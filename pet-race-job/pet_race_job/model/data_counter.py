from cassandra.cqlengine import columns
from cassandra.cqlengine.models import Model


# first, define a model
class DataCounter(Model):
    vtype = columns.Text(primary_key=True)
    value = columns.Counter()

    __keyspace__ = 'gpmr'