'''
entity PetCategories {
  petCategoryId UUID,
  name String,
  speed Float
}
'''

import uuid

from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


# first, define a model
class PetCategory(Model):
    petCategoryId = columns.UUID(primary_key=True, default=uuid.uuid4)
    name = columns.Text(required=True, index=True)
    speed = columns.Float(required=False)

    __keyspace__ = 'gpmr'
