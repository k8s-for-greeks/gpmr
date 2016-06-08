import uuid

from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


# first, define a model
class Metric(Model):
    metricId = columns.UUID(primary_key=True, default=uuid.uuid4)
    connectionErrors = columns.Integer()
    writeTimeouts = columns.Integer()
    readTimeouts = columns.Integer()
    unavailables = columns.Integer()
    otherErrors = columns.Integer()
    retries = columns.Integer()
    ignores = columns.Integer()
    knownHosts = columns.Integer()
    connectedTo = columns.Integer()
    openConnections = columns.Integer()
    reqCount = columns.Integer()
    reqMinLatency = columns.Double()
    reqMaxLatency = columns.Double()
    reqMeanLatency = columns.Double()
    reqStdev = columns.Double()
    reqMedian = columns.Double()
    req75percentile = columns.Double()
    req97percentile = columns.Double()
    req98percentile = columns.Double()
    req99percentile = columns.Double()
    req999percentile = columns.Double()
    dateCreated = columns.DateTime()

    __keyspace__ = 'gpmr'
