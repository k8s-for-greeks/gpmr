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
    numOfPets = columns.Integer(index=True)
    length = columns.Integer()
    description = columns.Text(required=False)
    winnerId = columns.UUID(index=True)
    winnerPetCategory = columns.Text(required=False)
    startTime = columns.DateTime()
    endTime = columns.DateTime()

    __keyspace__ = 'gpmr'
