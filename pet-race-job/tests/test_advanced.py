# -*- coding: utf-8 -*-

from pet_race_job.core import hmm

import unittest


class AdvancedTestSuite(unittest.TestCase):
    """Advanced test cases."""

    def test_thoughts(self):
        hmm()


if __name__ == '__main__':
    unittest.main()
