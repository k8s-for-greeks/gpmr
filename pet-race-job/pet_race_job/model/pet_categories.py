'''
entity PetCategories {
  petCategoryId UUID,
  name String
}
'''

import uuid

from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


# first, define a model
class PetCategories(Model):
    petCategoryId = columns.UUID(primary_key=True, default=uuid.uuid4)
    name = columns.Text(required=True, index=True)
    speed = columns.BigInt(required=False)


__keyspace__ = 'gpmr'
