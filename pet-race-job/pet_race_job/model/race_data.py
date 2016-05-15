'''
entity RaceData {
	raceDataId UUID,
  petId UUID,
  petName String,
  petCategory String,
  petCategoryId UUID,
  runnerPosition Integer,
  runnerSashColor String,
  interval Integer,
  RunnerDistance BigDecimal,
  startTime Date,
}
'''

import uuid
from cassandra.cqlengine import columns
from cassandra.cqlengine import connection
from datetime import datetime
from cassandra.cqlengine.management import sync_table
from cassandra.cqlengine.models import Model

#first, define a model
class RaceData(Model):
  raceDataId      = columns.UUID(primary_key=True, default=uuid.uuid4)
  petId           = columns.UUID(primary_key=True, default=uuid.uuid4)
  raceId          = columns.UUID(primary_key=True, default=uuid.uuid4)
  petName         = columns.Text(index=True)
  petType         = columns.Text(required=False)
  petColor        = columns.UUID(primary_key=True, default=uuid.uuid4)
  petCategory     = columns.Text(required=False)
  petCategoryId   = columns.UUID(primary_key=True, default=uuid.uuid4)
  interval        = columns.Integer(index=True)
  runnerPosition  = columns.Integer()
  runnerDistance  = columns.BigInt()
  time            = columns.DateTime()
  startTime       = columns.DateTime()
