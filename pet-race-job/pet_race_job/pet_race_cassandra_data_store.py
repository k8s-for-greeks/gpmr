from datetime import datetime

from cassandra import ConsistencyLevel
from cassandra.cqlengine.connection import get_cluster
from cassandra.cqlengine.connection import get_session
from cassandra.cqlengine.connection import set_session
from cassandra.cqlengine.connection import setup as setup_cass
from cassandra.util import uuid_from_time

from pet_race_job.model import *
import logging


class PetRaceCassandraDataStore(object):
    """
    This is not good code.  This is code tries to make a lot of calls to Cassandra.  We
    could use batch, but we want to create reads an writes to C*
    """
    seeds = []
    keyspace = 'gpmr'
    session = None
    cluster = None

    logger = None

    def __init__(self, seeds, keyspace):
        self.seeds = seeds
        self.keyspace = keyspace

        # TODO configure ConsitencyLevel
        setup_cass(self.seeds, self.keyspace,
                   consistency=ConsistencyLevel.ONE, lazy_connect=False,
                   retry_connect=True, metrics_enabled=True)
        # setup_cass(self.seeds, self.keyspace, consistency=ConsistencyLevel.ANY, lazy_connect=False, retry_connect=True)
        #setup_cass(self.seeds, self.keyspace, consistency=ConsistencyLevel.ONE, lazy_connect=False, retry_connect=True)
        self.session = get_session()
        set_session(self.session)
        self.cluster = get_cluster()
        self.logger = logging.getLogger('pet_race_job')
        # self.logger.debug("session created")

    @staticmethod
    def get_pets_by_name(pet_name):
        q = Pet.objects.filter(name=pet_name)

        try:
            p = q.get()
        except Pet.DoesNotExist:
            raise ValueError('pet not found: ', pet_name)

        # self.logger.debug("loaded pet", p)
        return p

    @staticmethod
    def increment_counter_by_name(name):
        c = DataCounter.objects(vtype=name).get()
        v = c.value + 1
        c.update(value=v)

    @staticmethod
    def get_pets_by_category_name(pet_cat_name):
        return Pet.objects(petCategoryName=pet_cat_name)

    @staticmethod
    def get_pet_category_by_name(category_name):
        q = PetCategory.objects.filter(name=category_name)

        try:
            pet_cat = q.get()
        except PetCategory.DoesNotExist:
            raise ValueError('category not found: ', category_name)

        # self.logger.debug("loaded cat")
        return pet_cat

    @staticmethod
    def create_race(length, description, pet_category_name):
        dt = datetime.utcnow()
        uuid = uuid_from_time(dt)

        # TODO this is loading all pets ... random number??
        race_pets = PetRaceCassandraDataStore.get_pets_by_category_name(pet_category_name)
        pet_c = PetRaceCassandraDataStore.get_pet_category_by_name(pet_category_name)

        participants = {}
        pet_ids = []
        for p in race_pets:
            pet_ids.append(str(p["petId"]))
            p_id = uuid_from_time(datetime.utcnow())

            participant = {
                'raceParticipantId': str(p_id),
                'petId': str(p["petId"]),
                'raceId': str(uuid),
                'petName': p["name"],
                'petCategoryName': pet_c['name'],
                'petCategoryId': str(pet_c['petCategoryId']),
                'startTime': dt,
                'endTime': None,
                'finished': False,
                'finished_position': None,
                'current_distance': 0
            }

            participants[str(p_id)] = participant

            RaceParticipant.create(
                raceParticipantId=p_id,
                petId=p["petId"],
                raceId=uuid,
                petName=p["name"],
                petCategoryName=pet_c['name'],
                petCategoryId=pet_c['petCategoryId'],
                startTime=dt
            )
            PetRaceCassandraDataStore.increment_counter_by_name('RaceParticipant')

        saved_race = {
            'raceId': str(uuid),
            'numOfPets': len(race_pets),
            'length': length,
            'description': description,
            'petCategoryId': str(pet_c['petCategoryId']),
            'petCategoryName': pet_c['name'],
            'startTime': dt,
            'racersIds': pet_ids,
            'baseSpeed': pet_c['speed']
        }

        Race.create(
            raceId=uuid,
            numOfPets=len(race_pets),
            length=length,
            description=description,
            petCategoryId=pet_c['petCategoryId'],
            petCategoryName=pet_c['name'],
            startTime=dt,
            racersIds=pet_ids,
            baseSpeed=pet_c['speed']
        )

        PetRaceCassandraDataStore.increment_counter_by_name('Race')

        # self.logger.debug("race created")
        # self.logger.debug("race created: %s", saved_race)
        # self.logger.debug("race created: %s", participants)

        return saved_race, participants

    @staticmethod
    def save_normal(normals, loc, scale, size, race_obj):
        """
        Saves a race_normal to C*

        raceNormalId = columns.UUID(primary_key=True, default=uuid.uuid4)
        raceId = columns.UUID(primary_key=True, default=uuid.uuid4)
        petCategoryName = columns.Text(required=False)
        petCategoryId = columns.UUID(primary_key=True, default=uuid.uuid4)
        currentTime = columns.DateTime()
        normals = columns.List()
        normalLoc = columns.Float()
        normalScale = columns.Float()
        normalSize = columns.Tuple()
        """
        dt = datetime.utcnow()
        uuid = uuid_from_time(datetime.utcnow())

        RaceNormal.create(
            raceNormalId=uuid,
            raceId=race_obj['raceId'],
            petCategoryName=race_obj['petCategoryName'],
            petCategoryId=race_obj['petCategoryId'],
            currentTime=dt,
            normals=normals,
            normalLoc=loc,
            normalScale=scale,
            normalSize=size
        )
        PetRaceCassandraDataStore.increment_counter_by_name('RaceNormal')
        # self.logger.debug("normal saved")

    @staticmethod
    def save_racer_finish(racer):
        # self.logger.debug(racer)
        RaceParticipant.objects(
            raceParticipantId=racer['raceParticipantId']
        ).update(
            finished=True,
            finishPosition=racer['finish_position'],
            finishTime=racer['finish_time']
        )

    @staticmethod
    def save_racer_current_point(current_race, racer, race_sample):
        uuid = uuid_from_time(datetime.utcnow())
        RaceData.create(
            raceDataId=uuid,
            petId=racer['petId'],
            raceId=current_race['raceId'],
            petName=racer['petName'],
            petCategoryName=current_race['petCategoryName'],
            petCategoryId=current_race['petCategoryId'],
            interval=race_sample['sample_iteration'],
            runnerDistance=race_sample['distance_this_sample'],
            runnerPreviousDistance=race_sample['previous_distance'],
            startTime=current_race['startTime'],
            finished=race_sample['finished']
        )
        PetRaceCassandraDataStore.increment_counter_by_name('RaceData')

    @staticmethod
    def update_race_winner(current_race):
        Race.objects(raceId=current_race['raceId']).update(winnerId=current_race['winnerId'])

    def update_race(self, current_race, current_racers):
        for key, value in current_racers.items():
            uuid = uuid_from_time(datetime.utcnow())
            RaceResult.create(
                raceResultId=uuid,
                raceId=current_race['raceId'],
                raceParticipantId=key,
                petName=value['petName'],
                petCategoryName=current_race['petCategoryName'],
                finishPosition=value['finish_position'],
                finishTime=value['finish_time'],
                startTime=current_race['startTime']
            )
            PetRaceCassandraDataStore.increment_counter_by_name('RaceResult')

        metrics = self.cluster.metrics.stats
        hosts = int(metrics.known_hosts())
        connected_to = int(metrics.connected_to())
        open_connections = int(metrics.open_connections())

        Metric.create(
            metricId=uuid_from_time(datetime.utcnow()),
            connectionErrors=metrics.connection_errors,
            writeTimeouts=metrics.write_timeouts,
            readTimeouts=metrics.read_timeouts,
            unavailables=metrics.unavailables,
            otherErrors=metrics.other_errors,
            retries=metrics.retries,
            ignores=metrics.ignores,
            knownHosts=hosts,
            connectedTo=connected_to,
            openConnections=open_connections,
            reqCount=metrics.request_timer['count'],
            reqMinLatency=metrics.request_timer['min'],
            reqMaxLatency=metrics.request_timer['max'],
            reqMeanLatency=metrics.request_timer['mean'],
            reqStdev=metrics.request_timer['stdev'],
            reqMedian=metrics.request_timer['median'],
            req75percentile=metrics.request_timer['75percentile'],
            req97percentile=metrics.request_timer['97percentile'],
            req98percentile=metrics.request_timer['98percentile'],
            req99percentile=metrics.request_timer['99percentile'],
            req999percentile=metrics.request_timer['999percentile'],
            dateCreated=datetime.utcnow(),
        )
