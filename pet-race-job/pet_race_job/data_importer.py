import csv
import glob
import logging
import os
from datetime import datetime

from cassandra import cluster as cluster
from cassandra.cqlengine.connection import get_session
from cassandra.cqlengine.connection import set_session
from cassandra.cqlengine.connection import setup as setup_cass
from cassandra.cqlengine.management import sync_table, drop_keyspace, create_keyspace_simple, \
    create_keyspace_network_topology
from cassandra.util import uuid_from_time

from pet_race_job.model import *

os.environ["CQLENG_ALLOW_SCHEMA_MANAGEMENT"] = "1"


class DataImporter(object):
    session = None
    keyspace = None
    seeds = None
    cass = None
    logger = None

    """ arguments: seed, keyspace """

    def __init__(self, **kwargs):

        # if kwargs is None:
        self.seeds = kwargs.get('seeds')
        self.keyspace = kwargs.get('keyspace')
        self.logger = logging.getLogger('pet_race_job.logger')
        super()

    def connect_cass(self):
        setup_cass(self.seeds, self.keyspace)
        self.session = get_session()
        set_session(self.session)

    def drop_keyspace(self):
        setup_cass(self.seeds, 'system')
        self.session = get_session()
        set_session(self.session)
        drop_keyspace(self.keyspace)
        self.logger.debug("ks dropped")

    def create_network_keyspace(self):
        cluster.max_schema_agreement_wait = 0
        setup_cass(self.seeds, 'system')
        self.session = get_session()
        set_session(self.session)
        dc_map = {'DC1-Data': 3, 'DC1-Analytics': 3}
        create_keyspace_network_topology(name=self.keyspace, dc_replication_map=dc_map)
        create_keyspace_simple(name=self.keyspace, replication_factor=3)
        self.logger.debug("ks network topo created")

    def create_keyspace(self):
        cluster.max_schema_agreement_wait = 0
        setup_cass(self.seeds, 'system')
        self.session = get_session()
        set_session(self.session)
        create_keyspace_simple(name=self.keyspace, replication_factor=3)
        self.logger.debug("ks created")

    def create_tables(self):
        self.connect_cass()
        cluster.max_schema_agreement_wait = 0
        sync_table(DataCounter)
        sync_table(PetCategory)
        sync_table(Pet)
        sync_table(RaceData)
        sync_table(RaceNormal)
        sync_table(RaceResult)
        sync_table(RaceParticipant)
        sync_table(Race)
        sync_table(Metric)
        self.logger.debug("tables created")

    def save_pets(self, pets_create, category_name):

        self.connect_cass()

        q = PetCategory.objects.filter(name=category_name)
        if len(q) is not 1:
            raise ValueError('category not found: ', category_name)
        pet_cat = q.first()

        for _p in pets_create:
            Pet.create(
                petId=uuid_from_time(datetime.utcnow()),
                name=_p['name'],
                description=_p['description'],
                petCategoryName=pet_cat['name'],
                petCategoryId=pet_cat['petCategoryId'],
                petSpeed=pet_cat['speed']
            )
            self.logger.debug("pet created: %s", _p['name'])

    def save_pet_categories(self, categories):

        self.connect_cass()

        for cat in categories:
            speed = float(cat['speed'])

            PetCategory.create(
                petCategoryId=uuid_from_time(datetime.utcnow()),
                name=cat['name'],
                speed=speed
            )
            self.logger.debug("pet cat created: %s", cat['name'])

    def save_counters(self):

        self.connect_cass()

        DataCounter(vtype='Race').update()
        DataCounter(vtype='RaceData').update()
        DataCounter(vtype='RaceNormal').update()
        DataCounter(vtype='RaceParticipant').update()
        DataCounter(vtype='RaceResult').update()
        self.logger.debug("counters created")

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

    def parse_pet(self, file):
        self.logger.debug(file)
        data = []
        with open(file, newline='') as csv_file:
            reader = csv.DictReader(csv_file, fieldnames=['name', 'description'])
            for row in reader:
                data.append(row)
        return data
