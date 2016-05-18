# -*- coding: utf-8 -*-

import unittest

from .context import pet_race as PetRace


class AdvancedTestSuite(unittest.TestCase):
    """Advanced test cases."""

    def test_race(self):
        arg = {}
        race = PetRace(arg)
        race.run()


if __name__ == '__main__':
    unittest.main()
