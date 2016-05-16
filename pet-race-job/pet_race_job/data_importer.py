from model import pet_categories, pets, race_data, race_participants, race
from cassandra.cqlengine.management import sync_table, drop_keyspace, create_keyspace_simple
from cassanrda.cqlengine.connection import set_session
from cassandra.util import uuid_from_time

import cassandra_driver, datetime, csv

class DataImporter(object):
  """docstring for """
  def __init__(self, *args, **kwargs):
    self.seeds = args['seeds']
    super()

  session = None
  keyspace = 'gpmr'
  seeds = None

  def connect(super,keyspace:None):
    self.cass = CassandraDriver({cassandra_seeds:super.seeds,keyspace:keyspace})
    self.session = cass.session()
    # self.session.set_keyspace('mykeyspace')
    set_session(session)

  def create_keyspace(self):
    self.connect()
    drop_keyspace(keyspace)
    create_keyspace_simple(keyspace)
    self.cass.shutdown()

  def create_tables(self):
    self.connect(keyspace)
    sync_table(pet_categories.PetCategories)
    sync_table(pets.Pets)
    sync_table(race_data.RaceData)
    sync_table(race_participants.RaceParticipants)
    sync_table(rac.Race)
    self.cass.shutdown()

  def load_pets(self,pets,category_name):
    self.connect(keyspace)

    petCategory = pet_categories.PetCategories.objects.filter(name=pet['category_name'])
    petCategory = petCategory[0]

    for pet in pets:
      pets.Pets.create(
          petId=uuid_from_time(datetime.utcnow(),
          name=pets['name'],
          petCategory=petCategory['name'],
          petCategoryId = petCategory['petCategoryId']
          petSpeed = petCategory['speed']
        )

    self.cass.shutdown()

  def load_pet_categories(self,categories):
    self.connect(keyspace)

    for cat in categories:
      pet_categories.PetCategories.create(
        petCategoryId=uuid_from_time(datetime.utcnow(),
        name=cat['name']
        speed=cat['speed']
      )

    self.cass.shutdown()

if __name__ == '__main__':
  loader = DataImporter(['cassandra-0','cassandra-1'])
  #loader.create_keyspace()
  loader.create_tables()

  with open('../data/pet_categores.csv', newline='') as csvfile:
      data = csv.DictReader(csvfile)
      # TODO here
