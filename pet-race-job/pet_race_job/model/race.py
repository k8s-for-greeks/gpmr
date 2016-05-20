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
from cassandra.cqlengine.models import Model


# first, define a model
class Race(Model):
    raceId = columns.UUID(primary_key=True, default=uuid.uuid4)
    petCategoryId = columns.UUID(index=True, default=uuid.uuid4, required=True)
    petCategoryName = columns.Text()
    numOfPets = columns.Integer(index=True, required=True)
    length = columns.Integer(required=True)
    description = columns.Text(required=False)
    winnerId = columns.UUID(index=True)
    winnerPetCategory = columns.Text(required=False)
    startTime = columns.DateTime(required=True)
    endTime = columns.DateTime(),
    racersIds = columns.List(required=True, value_type=columns.UUID())
    baseSpeed = columns.Float(required=True)

    __keyspace__ = 'gpmr'
