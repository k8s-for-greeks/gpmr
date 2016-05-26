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
from cassandra.cqlengine.models import Model


# first, define a model
class RaceParticipants(Model):
    raceParticipantsId = columns.UUID(primary_key=True, default=uuid.uuid4)
    petId = columns.UUID(primary_key=True, default=uuid.uuid4)
    raceId = columns.UUID(index=True, default=uuid.uuid4)
    petName = columns.Text(index=True)
    petColor = columns.UUID(index=True, default=uuid.uuid4)
    petCategoryName = columns.Text(required=False)
    petCategoryId = columns.UUID(index=True, default=uuid.uuid4)
    startTime = columns.DateTime()
    finishTime = columns.Decimal()
    finishPosition = columns.Integer()
    finished = columns.Boolean(index=True)

    __keyspace__ = 'gpmr'
