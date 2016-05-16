'''
entity Race {
	id UUID,
  numOfPets Integer,
  length Integer,
	winnerId UUID,
  winnerName String,
  winnnerPetCategory String,
	startTime Date,
	endTime Date
'''

import uuid
from cassandra.cqlengine import columns
from cassandra.cqlengine import connection
from datetime import datetime
from cassandra.cqlengine.management import sync_table
from cassandra.cqlengine.models import Model

#first, define a model
class Race(Model):
  raceId              = columns.UUID(primary_key=True, default=uuid.uuid4)
  numOfPets           = columns.Integer(index=True)
  length              = columns.Integer(index=True)
  description         = columns.Text(required=False)
  winnerId            = columns.UUID(primary_key=True, default=uuid.uuid4)
  winnnerPetCategory  = columns.Text(required=False)
  startTime           = columns.DateTime()
  endTime             = columns.DateTime()
	
	__keyspace__ = 'gpmr'
