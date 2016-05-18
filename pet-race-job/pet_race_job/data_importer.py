import csv
import glob
import os
from datetime import datetime

from cassandra.cqlengine.connection import set_session
from cassandra.cqlengine.management import sync_table, drop_keyspace, create_keyspace_simple
from cassandra.util import uuid_from_time

from .cassandra_driver import CassandraDriver
from .model import pet_categories, pets, race_data, race_participants, race


class DataImporter(object):
    session = None
    keyspace = None
    seeds = None
    cass = None
    logger = None

    """ arguments: seed, keyspace """
    def __init__(self, **kwargs):
        super()
        self.seeds = kwargs.get('seeds')
        self.keyspace = kwargs.get('keyspace')
        self.cass = CassandraDriver({"cassandra_seeds": self.seeds, "keyspace": self.keyspace})
        self.session = self.cass.session()
        self.logger = logging.getLogger('pet_race_job')
        set_session(self.session)

    def create_keyspace(self):
        self.cass.connect()
        drop_keyspace(self.keyspace)
        create_keyspace_simple(name=self.keyspace, replication_factor=3)
        self.logger.debug("ks created")

    def create_tables(self):
        self.cass.connect(self.keyspace)
        sync_table(pet_categories.PetCategories)
        sync_table(pets.Pets)
        sync_table(race_data.RaceData)
        sync_table(race_participants.RaceParticipants)
        sync_table(race.Race)
        self.logger.debug("tables created")

    def save_pets(self, pets_create, category_name):
        self.cass.connect(self.keyspace)

        pet_cat = pet_categories.PetCategories.objects.filter(name=category_name)
        pet_cat = pet_cat[0]

        for pet in pets_create:
            pets.Pets.create(
                petId=uuid_from_time(datetime.utcnow()),
                name=pet['name'],
                petCategory=pet_cat['name'],
                petCategoryId=pet_cat['petCategoryId'],
                petSpeed=pet_cat['speed']
            )
            self.logger.debug("pet created: %s", pet['name'])

    def save_pet_categories(self, categories):
        self.cass.connect(self.keyspace)

        for cat in categories:
            pet_categories.PetCategories.create(
                petCategoryId=uuid_from_time(datetime.utcnow()),
                name=cat['name'],
                speed=cat['speed']
            )
            self.logger.debug("pet cat created: %s", cat['name'])

    @staticmethod
    def parse_pet_categories():
        with open('../data/pet_categores.csv', newline='') as csv_file:
            return csv.DictReader(csv_file)

    @staticmethod
    def parse_pet(file):
        with open(file, newline='') as csv_file:
            return csv.DictReader(csv_file, fieldnames=['name', 'description'])


if __name__ == '__main__':
    loader = DataImporter(seeds=['cassandra-0', 'cassandra-1'], keyspace='gpmr')
    loader.create_keyspace()
    loader.create_tables()

    pet_cats = loader.parse_pet_categories()
    loader.save_pet_categories(pet_cats)

    pet_files = glob.glob("../data/pets/")

    for pet_file in pet_files:
        pet_data = loader.parse_pet(pet_file)
        filename, file_extension = os.path.splitext(pet_file)
        filename = filename.split('/')[-1]
        loader.save_pets(pet_data, filename)

    loader.cass.shutdown()
