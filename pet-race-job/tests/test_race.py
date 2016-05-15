# -*- coding: utf-8 -*-

from .context import pet_race_job

import unittest

class AdvancedTestSuite(unittest.TestCase):
    """Advanced test cases."""

    def test_race(self):
      race = pet_race_job.pet_race.PetRace()
      race.run()


if __name__ == '__main__':
    unittest.main()
