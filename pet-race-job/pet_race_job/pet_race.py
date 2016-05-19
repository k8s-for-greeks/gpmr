import logging

import numpy


class PetRace(object):
    """docstring for """
    data_source = ()
    # self.arg = arg

    # guid list of racers still running
    racers_still_running = []

    # order list of finishers {guid: guid, total_distance: total_distance}
    racers_finished = []

    # racers with guid, race_guid, current_distance
    # {guid:guid, {current_distance: 42,
    #              finished: False, finished_position: 1, name: name, monster_type: guid
    #              racers_positions_by_time : [{postion: 1, velocity: 42, total_traveled: 84 }]
    #             }}
    racers = {}

    # includes race_guid, distance
    # {guid: 42, length: 4, location: guid}
    race = {}

    # list of lists
    # point in time with each poll spot
    racers_positions_by_time = [[]]

    logger = None

    normal_scale = None
    base_racer_speed = 5

    def __init__(self, **kwargs):
        self.race = kwargs.get('race')
        self.base_racer_speed = self.race['baseSpeed']
        self.normal_scale = kwargs.get('normal_scale')
        self.racers = kwargs.get('racers')
        self.data_source = kwargs.get('data_source')
        self.logger = logging.getLogger('pet_race_job')
        super()

    # I am thinking this is a possible idea?
    # http://docs.scipy.org/doc/numpy-1.10.1/reference/generated/numpy.random.normal.html
    # what do we set loc,scale,and size to?
    def numpy_normal(self, normal_size, loc=None, scale=None):

        if loc is None:
            loc = self.base_racer_speed
        if scale is None:
            scale = self.normal_scale

        random_normal = numpy.random.normal(loc=loc, scale=scale, size=normal_size)
        self.save_normal(random_normal, loc, scale, normal_size)
        print(random_normal)
        return random_normal

    # TODO
    def save_normal(self, normals, loc, scale, size):
        self.data_source.save_normal(normals, loc, scale, size, self.race)

    # TODO
    def save_racer_current(self, racer_guid, finished):
        racer = self.racers[racer_guid]
        self.data_source.save_racer_current(racer, self.race, finished)

    # TODO
    def save_racer_finish(self, racer_guid):
        racer = self.racers[racer_guid]
        self.data_source.save_racer_finish(racer, self.race)

    # TODO
    def save_racer_current_point(self, racer_guid):
        racer = self.racers[racer_guid]
        self.data_source.save_racer_current_point(racer, self.race)

    def save_race(self):
        self.data_source.save_race(self.race, self.racers)

    @staticmethod
    def either_race_distance_current(race_distance, current_distance):
        if current_distance > race_distance:
            return race_distance, True
        else:
            return race_distance, False

    # total number of seconds running + how long it took to finish
    # last sample of race
    def calc_finish_time(self, racer_guid):
        racer = self.racers[racer_guid]
        return 42

    def run_race(self):
        logging.debug("Starting a race")
        # current_positions = []
        while True:

            racers_finished_this_iteration = []

            # calculate normals and save them
            random_normal = self.numpy_normal(len(self.racers_still_running))

            for racer in self.racers_still_running:

                race_distance = self.race['distance']
                previous_distance = self.racers[racer]['current_distance']
                current_racer_distance = previous_distance + random_normal.pop()
                current_racer_distance_adj, finished = self.either_race_distance_current(race_distance,
                                                                                         current_racer_distance)
                self.racers[racer]['current_distance'] = current_racer_distance_adj
                self.racers[racer]['finished'] = finished

                # TODO
                # store race interval in racer
                # no position here
                self.save_racer_current(racer, finished)

                if finished:
                    racers_finished_this_iteration.append(racer)
                    self.racers_still_running.remove(racer)
                    self.racers[racer]['total_time'] = self.calc_finish_time(self.racers[racer])
                    self.racers[racer]['total_distance'] = current_racer_distance
                    self.racers[racer]['finished'] = finished
                    # TODO
                    self.save_racer_finish(racer)

                    # TODO
                self.save_racer_current_point(racer)

                # how the heck do we do this??
                # current_positions = sorted(racers_still_running, key=itemgetter('total_distance'), reverse=True)
                # add each iteration add the racers that have finished this iteration into the finished lists
                # TODO save finished data

            if len(self.racers_still_running) == 0:
                break
            # end while
            self.save_race()
            # TODO save race data
