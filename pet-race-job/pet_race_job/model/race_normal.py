'''
entity RaceNormal {
  raceNormalId UUID,
  raceId UUID,
  petCategoryName String,
  petCategoryId UUID,
  currentTime Date,
  normals = List,
  normalLoc = Float,
  normalScale = Float,
  normalSize = Set,
  endTime   Date
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
    normals = columns.List()
    normalLoc = columns.Float()
    normalScale = columns.Float()
    normalSize = columns.Tuple()

    __keyspace__ = 'gpmr'
