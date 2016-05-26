# -*- coding: utf-8 -*-

import unittest
import logging

from pet_race_job.pet_race import PetRace


class RaceTestSuite(unittest.TestCase):
    """Advanced test cases."""

    def test_race(self):

        self.logger = logging.getLogger('pet_race_job')
        seeds = ['cassandra-0.cassandra.default.svc.cluster.local',
                 'cassandra-1.cassandra.default.svc.cluster.local']
        keyspace = 'gpmr'
        self.logger.debug("creating obj")
        pet_race = PetRace(seeds=seeds, keyspace=keyspace)
        self.logger.debug("creating race")
        pet_race.create_race(100, "description", "Giants", 1)
        self.logger.debug("running race")
        pet_race.run_race()


if __name__ == '__main__':
    unittest.main()
