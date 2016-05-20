from cassandra.cluster import Cluster


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
        self.session = self.cluster.connect(self.keyspace)
        print("session: ", self.session)

    def shutdown(self):
        self.cluster.shutdown()

    def connect_cass(self, key_space=None):
        if key_space is None:
            key_space = 'system'
        return self.cluster.connect(key_space)


    # TODO
    def save_normal(self, normal, race):
        return

    # TODO
    def save_racer_current(self, racer, finished):
        return

    # TODO
    def save_racer_finish(self, racer):
        return

    # TODO
    def save_racer_current_point(self, racer):
        return

    def save_race(self, race, racers):
        return
