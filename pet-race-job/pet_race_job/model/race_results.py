'''
entity RaceResults {
  raceResultsId UUID,
  petId UUID,
  petName String,
  petCategory String,
  petCategoryId UUID,
  place Sting,
  startTime Date,
  finishTime Decimal,
  position Integer
}
'''

import uuid

from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


# first, define a model
class RaceResults(Model):
    raceResultsId = columns.UUID(primary_key=True, default=uuid.uuid4)
    raceId = columns.UUID(primary_key=True, default=uuid.uuid4)
    raceParticipantsId = columns.UUID(primary_key=True, default=uuid.uuid4)
    petName = columns.Text(index=True)
    petType = columns.Text(required=False)
    petColor = columns.UUID(primary_key=True, default=uuid.uuid4)
    petCategoryName = columns.Text(required=False)
    petCategoryId = columns.UUID(primary_key=True, default=uuid.uuid4)
    finishPosition = columns.Integer()
    finishTime = columns.Decimal()
    startTime = columns.DateTime()

    __keyspace__ = 'gpmr'
