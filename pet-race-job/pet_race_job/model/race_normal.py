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
    raceId = columns.UUID(index=True, default=uuid.uuid4)
    petCategoryId = columns.UUID(index=True, default=uuid.uuid4)
    petCategoryName = columns.Text(index=True,required=False)
    currentTime = columns.DateTime()
    normals = columns.List(value_type=columns.Decimal())
    normalLoc = columns.Float()
    normalScale = columns.Float()
    normalSize = columns.Integer()

    __keyspace__ = 'gpmr'
