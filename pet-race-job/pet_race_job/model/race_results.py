'''
entity RaceResults {
  raceResultsId UUID,
  petId UUID,
  petName String,
  petCategory String,
  petCategoryId UUID,
  place Sting,
  startTime Date,
  endTime   Date
}
'''

import uuid

from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


# first, define a model
class RaceResults(Model):
    raceResultsId = columns.UUID(primary_key=True, default=uuid.uuid4)
    raceId = columns.UUID(primary_key=True, default=uuid.uuid4)
    petId = columns.UUID(primary_key=True, default=uuid.uuid4)
    petName = columns.Integer(index=True)
    petType = columns.Text(required=False)
    petColor = columns.UUID(primary_key=True, default=uuid.uuid4)
    petCategory = columns.Text(required=False)
    petCategoryId = columns.UUID(primary_key=True, default=uuid.uuid4)
    endTime = columns.DateTime()

    __keyspace__ = 'gpmr'
