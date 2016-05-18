import csv
import glob
import logging
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

        if kwargs is None:
            self.seeds = kwargs.get('seeds')
            self.keyspace = kwargs.get('keyspace')
            self.cass = CassandraDriver({"cassandra_seeds": self.seeds, "keyspace": self.keyspace})
            self.session = self.cass.session()
            set_session(self.session)

        self.logger = logging.getLogger('pet_race_job')

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

    def parse_pet_files(self, d):
        files = glob.glob(d)
        _pets = []
        for pet_f in files:
            self.logger.debug(pet_f)
            data = self.parse_pet(pet_f)
            fn, fx = os.path.splitext(pet_f)
            fn = fn.split('/')[-1]
            _pets.append({'pet': data, 'cat': fn})
        return _pets

    @staticmethod
    def parse_pet_categories(file):
        data = []
        with open(file, newline='') as csv_file:
            reader = csv.DictReader(csv_file)
            for row in reader:
                data.append(row)
        return data

    def parse_pet(self,file):
        self.logger.debug(file)
        data = []
        with open(file, newline='') as csv_file:
            reader = csv.DictReader(csv_file, fieldnames=['name', 'description'])
            for row in reader:
                data.append(row)
        return data


if __name__ == '__main__':
    loader = DataImporter(seeds=['cassandra-0', 'cassandra-1'], keyspace='gpmr')
    loader.create_keyspace()
    loader.create_tables()

    pet_cats = loader.parse_pet_categories('../data/pet_categories.csv')
    loader.save_pet_categories(pet_cats)

    pets = loader.parse_pet_files('../data/pets/*.csv')

    for pet in pets:
        loader.save_pets(pet['pet'], pet['cat'])

    loader.cass.shutdown()
