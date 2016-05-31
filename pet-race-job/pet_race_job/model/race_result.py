'''
entity RaceResult {
  raceResultId UUID,
  raceId UUID,
  petCategoryId UUID,
  raceParticipantId UUID,
  petName String,
  petType String,
  petColor UUID,
  petCategoryName String,
  finishPosition Integer,
  finishTime Decimal,
  startTime DateTime
}
'''

import uuid

from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


# first, define a model
class RaceResult(Model):
    raceResultId = columns.UUID(primary_key=True, default=uuid.uuid4)
    raceId = columns.UUID(primary_key=True, default=uuid.uuid4)
    raceParticipantId = columns.UUID(primary_key=True, default=uuid.uuid4)
    petName = columns.Text(index=True)
    petType = columns.Text(required=False)
    petColorId = columns.UUID(index=True, default=uuid.uuid4)
    petCategoryName = columns.Text(required=False)
    petCategoryId = columns.UUID(primary_key=True, default=uuid.uuid4)
    finishPosition = columns.Integer()
    finishTime = columns.Decimal()
    startTime = columns.DateTime()

    __keyspace__ = 'gpmr'
