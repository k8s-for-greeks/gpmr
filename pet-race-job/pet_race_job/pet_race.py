from operator import itemgetter
from numpy import numpy


class PetRace(object):
  """docstring for """
  def __init__(self, arg):
    #super(, self).__init__()
    return
    #self.arg = arg

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
  base_racer_speed = 5

  # includes race_guid, distance
  # {guid: 42, length: 4, location: guid}
  race = {}

  # list of lists
  # point in time with each poll spot
  racers_positions_by_time = [[]]

  def numpy_normal(self,normal_size):

    # I am thinking this is a possible idea?
    # http://docs.scipy.org/doc/numpy-1.10.1/reference/generated/numpy.random.normal.html
    # what do we set loc,scale,and size to?
    random_normal = numpy.random.normal(loc=0.0, scale=1.0, size=normal_size)
    return random_normal

  # TODO
  def save_normal(self, normal):

    return

  # TODO
  def save_racer_current(self,racer_guid):
    racer = self.racers[racer_guid]
    return

  def either_race_distance_current(self,race_distance, current_distance):
    if current_distance > race_distance:
      return race_distance, True
    else:
      return race_distance, False

  # total number of seconds running + how long it took to finish
  # last sample of race
  def calc_finish_time(self,racer):
    return

  # TODO
  def save_racer_finish(self,racer):
    return

  #TODO
  def save_racer_current_point(self,racer):
    return

  def save_race():
    return

  def run(self):
    current_positions = []
    while True:
      racers_finished_this_iteration = []
      random_normal = self.numpy_normal(len(self.racers_still_running))
      save_normal(random_normal)
      for racer in self.racers_still_running:
          # may not have to do this if normal takes a base value
        current_racer_speed = random_normal.pop() * self.base_racer_speed
        race_distance = self.race['distance']
        previous_distance = self.racers[racer]['current_distance']
        current_racer_distance = previous_distance + current_racer_speed
        current_racer_distance_adj, finished = either_race_distance_current( race_distance, current_racer_distance)
        self.racers[racer]['current_distance'] = current_racer_distance_adj
        self.racers[racer]['finished'] = finished

          # store race interval in racer

          # no position here
        save_racer_current(racer, finished)

        if finished:
          racers_finished_this_iteration.add(racer)
          self.racers_still_running.remove(racer)
          self.racers[racer]['total_time'] = calc_finish_time(racers[racer])
          self.racers[racer]['total_distance'] = current_racer_distance
          self.racers[racer]['finished'] = finished
          # TODO
          save_racer_finish(racers[racer])

          # TODO
        save_racer_current_point(racers[racer])


        # how the heck do we do this??
        # current_positions = sorted(racers_still_running, key=itemgetter('total_distance'), reverse=True)
        # add each iteration add the racers that have finished this iteration into the finished lists
        # TODO save finished data


      if len(self.racers_still_running) == 0:
        break
      # end while
      save_race()
      # TODO save race data
