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
from cassandra.cqlengine import connection
from datetime import datetime
from cassandra.cqlengine.management import sync_table
from cassandra.cqlengine.models import Model

#first, define a model
class Pets(Model):
  petId         = columns.UUID(primary_key=True, default=uuid.uuid4)
  name          = columns.Text(required=False)
  petCategory   = columns.Text(required=False)
  petCategoryId = columns.UUID(required=False)
  petSpeed      = columns.BigInt()

	__keyspace__ = 'gpmr'

	# TODO
	#__options__ = {'compaction': {'class': 'LeveledCompactionStrategy',
  #                                'sstable_size_in_mb': '64',
  #                                'tombstone_threshold': '.2'},
  #                 'read_repair_chance': '0.5',
  #                 'comment': 'User data stored here'}
