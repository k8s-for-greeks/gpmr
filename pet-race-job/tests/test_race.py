# -*- coding: utf-8 -*-

from .context import pet_race as PetRace

import unittest

class AdvancedTestSuite(unittest.TestCase):
    """Advanced test cases."""

    def test_race(self):
      arg = {}
      race = PetRace(arg)
      race.run()


if __name__ == '__main__':
    unittest.main()
