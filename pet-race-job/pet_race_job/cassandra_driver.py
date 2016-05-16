
# how do a I do a callback to shutdown the session gracefully?
class CassandraDriver(object):
  keyspace = None
  seeds = None
  cluster = None
  session = None

  """docstring for """
  def __init__(self, arg):
    super()
    self.seeds = arg['cassandra_seeds']
    self.cluster = Cluster(self.seeds)
    self.keyspace = arg['keyspace']
    self.session = cluster.connect(self.keyspace)
    return

  def shutdown(self):
    self.cluster.shutdown()
