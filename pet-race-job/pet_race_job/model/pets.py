'''
entity Pets {
  petId UUID,
  name String,
  petCategory String,
  petCategoryId UUID,
  petSpeed BigDecimal
}
'''

import uuid

from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


# Pets ... lots of pets
class Pets(Model):
    petId = columns.UUID(primary_key=True, default=uuid.uuid4)
    name = columns.Text(required=True, index=True)
    description = columns.Text(required=False)
    petCategoryName = columns.Text(required=True, index=True)
    petCategoryId = columns.UUID(required=True, index=True)
    petSpeed = columns.Float(required=True)

    __keyspace__ = 'gpmr'

# TODO
# __options__ = {'compaction': {'class': 'LeveledCompactionStrategy',
#                                'sstable_size_in_mb': '64',
#                                'tombstone_threshold': '.2'},
#                 'read_repair_chance': '0.5',
#                 'comment': 'User data stored here'}
