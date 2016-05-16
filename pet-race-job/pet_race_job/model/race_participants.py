'''
entity RaceParticipants {
	id UUID,
  petId UUID,
  petName String,
  petType String,
  petColor String,
  petCategory String,
  petCategoryId String,
  raceId UUID
}
'''

import uuid
from cassandra.cqlengine import columns
from cassandra.cqlengine import connection
from datetime import datetime
from cassandra.cqlengine.management import sync_table
from cassandra.cqlengine.models import Model

#first, define a model
class RaceParticipants(Model):
  raceParticipantsId  = columns.UUID(primary_key=True, default=uuid.uuid4)
  petId               = columns.UUID(primary_key=True, default=uuid.uuid4)
  raceId              = columns.UUID(primary_key=True, default=uuid.uuid4)
  petName             = columns.Integer(index=True)
  petType             = columns.Text(required=False)
  petColor            = columns.UUID(primary_key=True, default=uuid.uuid4)
  petCategory         = columns.Text(required=False)
  petCategoryId       = columns.UUID(primary_key=True, default=uuid.uuid4)
  endTime             = columns.DateTime()

	__keyspace__ = 'gpmr'
