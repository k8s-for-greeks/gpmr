import logging
from decimal import *
from operator import itemgetter

import numpy

from pet_race_job.pet_race_cassandra_data_store import PetRaceCassandraDataStore


class PetRace(object):
    data_source = ()

    # guid list of racers still running
    racers_still_running = []

    # order list of finishers {guid: guid, total_distance: total_distance}
    racers_finished = []

    # racers with guid, race_guid, current_distance
    # {guid:guid, {current_distance: 42,
    #              finished: False, finished_position: 1, name: name, monster_type: guid
    #             }}
    racers = {}

    # includes race_guid, distance
    # {guid: 42, length: 4, location: guid}
    race = {}

    logger = None

    normal_scale = None
    base_racer_speed = None

    race_length = None

    def __init__(self, seeds, keyspace):
        self.logger = logging.getLogger('pet_race_job')
        self.data_source = PetRaceCassandraDataStore(seeds, keyspace)
        self.logger.debug("race __init__")
        super()

    def create_race(self, length, description, pet_category_name, normal_scale):
        """
        Create a race
        :param length: length in meters
        :param description: Description of the Race
        :param pet_category_name: Pet Category Name
        :param normal_scale: Normal scale used to calc numpy.random.normal
        :return:
        """
        self.normal_scale = normal_scale
        race_created, racers = self.data_source.create_race(
            length=length, description=description, pet_category_name=pet_category_name)
        self.race_length = Decimal(length)
        self.racers = racers
        self.racers_still_running = list(racers.keys())
        self.race = race_created
        self.base_racer_speed = self.race['baseSpeed']
        self.logger.debug("created race")

    def numpy_normal(self, normal_size, loc=None, scale=None):
        """
        http://docs.scipy.org/doc/numpy-1.10.1/reference/generated/numpy.random.normal.html

        :param normal_size:
        :param loc:
        :param scale:
        :return:
        """

        if loc is None:
            loc = self.base_racer_speed
        if scale is None:
            scale = self.normal_scale

        if normal_size <= 0:
            raise "normal_size cannot be <= 0"

        random_normal = numpy.random.normal(loc=loc, scale=scale, size=normal_size)

        self.save_normal(random_normal, loc, scale, normal_size)

        n = random_normal.tolist()

        if len(n) <= 0:
            raise "normal cannot be zero"

        return n

    def save_normal(self, normals, loc, scale, size):
        """
        Save a normal to C*
        :param normals:
        :param loc:
        :param scale:
        :param size:
        :return:
        """
        self.data_source.save_normal(normals, loc, scale, size, self.race)

    def save_racer_finish(self, racer_id):
        """
        Save the racer to C*
        :param racer_id:
        :return:
        """
        racer = self.racers[racer_id]
        self.data_source.save_racer_finish(racer)

    def save_racer_current_point(self, racer, race_sample):
        """
        Save RaceData to C*
        :param racer:
        :param race_sample:
        :return:
        """
        self.data_source.save_racer_current_point(self.race, racer, race_sample)

    def update_race(self, race, racers):
        """
        Save RaceResult to C*
        :param race:
        :param racers:
        :return:
        """
        self.data_source.update_race(race, racers)

    def update_race_winner(self):
        """
        Update Race obj with winner
        :return:
        """
        self.data_source.update_race_winner(self.race)

    @staticmethod
    def either_race_distance_current(race_distance, current_distance):
        if current_distance >= race_distance:
            return race_distance, True
        else:
            return current_distance, False

    @staticmethod
    def calc_finish_time(previous_distance, distance_this_sample, number_of_seconds, race_length):
        """
        Calculate finish time of a racer
        :param previous_distance:
        :param distance_this_sample:
        :param number_of_seconds:
        :param race_length:
        :return:
        """
        previous_distance = Decimal(previous_distance)
        distance_this_sample = Decimal(distance_this_sample)
        number_of_seconds = Decimal(number_of_seconds)
        distance_ran_to_finish = race_length - previous_distance
        time_this_sample = distance_ran_to_finish / distance_this_sample
        finish_time = number_of_seconds + time_this_sample

        return finish_time

    def run_race(self):
        """
        Run a race!
        :return:
        """

        logging.debug("Starting the race")

        # each while loop is a second
        seconds = 1
        # position is incremented as each racer finished
        position = 1
        racers_finished_times = []

        # We race till we are done

        while True:

            racers_finished_this_iteration = []

            # Get random numbers from a normal
            random_normal = self.numpy_normal(len(self.racers_still_running))

            # take samples for each runner that is still running
            for racer in self.racers_still_running:

                racer_obj = self.racers[racer]
                previous_distance = racer_obj['current_distance']
                distance_this_sample = Decimal(random_normal.pop())
                current_racer_distance = previous_distance + Decimal(distance_this_sample)

                # determine if the racers has finished
                current_racer_distance_adj, finished = self.either_race_distance_current(self.race_length,
                                                                                         current_racer_distance)
                self.racers[racer]['current_distance'] = current_racer_distance_adj
                self.racers[racer]['finished'] = finished

                race_sample = {
                    'racerId': racer,
                    'finished': finished,
                    'current_distance': current_racer_distance_adj,
                    'current_distance_all': current_racer_distance,
                    'previous_distance': previous_distance,
                    'distance_this_sample': distance_this_sample,
                    'sample_iteration': seconds
                }

                if finished:
                    finish_time = self.calc_finish_time(previous_distance, distance_this_sample, seconds,
                                                        self.race_length)
                    racers_finished_this_iteration.append(racer)
                    race_sample['finish_time'] = finish_time
                    self.racers[racer]['finish_time'] = finish_time
                    self.racers[racer]['total_distance'] = current_racer_distance
                    self.racers[racer]['finished'] = finished
                    racers_finished_times.append({
                        'racerId': racer,
                        'finish_time': finish_time,
                    })

                self.save_racer_current_point(self.racers[racer], race_sample)

            # python magic that gives us the racers that are not finished
            self.racers_still_running = [x for x in self.racers_still_running
                                         if x not in racers_finished_this_iteration]

            # sort finished racers based on times
            positions = sorted(racers_finished_times, key=itemgetter('finish_time'))

            # save each runner positions
            for idx, r in enumerate(positions):
                racer_id = r['racerId']
                self.racers[racer_id]['finish_position'] = position

                # position carries through each while loop, and is set before the start
                if position == 1:
                    self.race['winnerId'] = racer_id
                    self.update_race_winner()

                self.logger.debug("current position %s, time: %s", position, r['finish_time'])
                self.save_racer_finish(racer_id)

                # position carries through each while loop, and is set before the start
                position += 1

            racers_finished_times = []

            # n is used for how many loops i.e. seconds have passed
            seconds += 1

            if len(self.racers_still_running) == 0:
                break
        # end while

        self.update_race(self.race, self.racers)
        self.logger.debug("completed")
