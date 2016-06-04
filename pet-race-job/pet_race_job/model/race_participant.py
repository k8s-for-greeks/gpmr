'''
entity RaceParticipants {
 raceParticipantId UUID,
 petId UUID,
 raceId UUID,
 petName String,
 petColor UUID,
 petCategoryName String,
 petCategoryId UUID,
 startTime DateTime,
 finishTime Decimal,
 finishPosition Integer,
 finished Boolean
}
'''

import uuid

from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


# first, define a model
class RaceParticipant(Model):
    raceParticipantId = columns.UUID(primary_key=True, default=uuid.uuid4)
    petId = columns.UUID(index=True, default=uuid.uuid4)
    raceId = columns.UUID(index=True, default=uuid.uuid4)
    petName = columns.Text(index=True)
    petColor = columns.Text(index=True)
    petCategoryName = columns.Text(required=False)
    petCategoryId = columns.UUID(index=True, default=uuid.uuid4)
    startTime = columns.DateTime()
    finishTime = columns.Decimal()
    finishPosition = columns.Integer()
    finished = columns.Boolean(index=True)

    __keyspace__ = 'gpmr'
