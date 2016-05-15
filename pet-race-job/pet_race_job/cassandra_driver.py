

class CassandraDriver(object):
  """docstring for """
  def __init__(self, arg):
    super()
    self.seeds = arg['cassandra_seeds']
    self.cluster = Cluster(self.seeds)
    self.keyspace = arg['keyspace']
    self.session = cluster.connect(self.keyspace)
    return

  keyspace = None
  seeds = []
  cluster = {}

  
