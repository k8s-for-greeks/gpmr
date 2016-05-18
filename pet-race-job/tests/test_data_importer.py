# -*- coding: utf-8 -*-

import unittest

from .context import data_importer as DataImporter


class DataImporterTestSuite(unittest.TestCase):
    """Advanced test cases."""

    def test_data_importer(self):
        di = DataImporter()

    def test_data_importer_parse_pet_cats(self):
        di = DataImporter()
        di.parse_pet_categories('data/pet_categories.csv')

    def test_data_importer_parse_pets(self):
        di = DataImporter()
        di.parse_pet('data/pets/Eagles.csv')

    def test_data_importer_parse_pets_dir(self):
        di = DataImporter()
        pets = di.parse_pet_files('data/pets/*.csv')

if __name__ == '__main__':
    unittest.main()
