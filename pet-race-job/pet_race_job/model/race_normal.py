'''
entity RaceNormal {
 raceNormalId UUID,
 raceId UUID,
 petCategoryId UUID,
 petCategoryName String,
 currentTime DateTime,
 normals List<Float>,
 normalLoc Float,
 normalScale Float,
 normalSize Integer
}
'''

import uuid

from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


class RaceNormal(Model):
    raceNormalId = columns.UUID(primary_key=True, default=uuid.uuid4)
    raceId = columns.UUID(primary_key=True, default=uuid.uuid4)
    petCategoryName = columns.Text(required=False)
    petCategoryId = columns.UUID(primary_key=True, default=uuid.uuid4)
    currentTime = columns.DateTime()
    normals = columns.List(value_type=columns.Float())
    normalLoc = columns.Float()
    normalScale = columns.Float()
    normalSize = columns.Integer()

    __keyspace__ = 'gpmr'
