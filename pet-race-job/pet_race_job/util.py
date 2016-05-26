from cassandra.cqlengine.connection import get_session
from cassandra.cqlengine.connection import set_session
from cassandra.cqlengine.connection import setup as setup_cass


def connect_cass(seeds, keyspace):
    setup_cass(seeds, keyspace)
    session = get_session()
    set_session(session)
    return session
