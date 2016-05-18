import logging

from .cassandra_driver import CassandraDriver as cassandra_driver
from .core import hmm
from .model.pet_categories import PetCategories as pet_categories
from .model.pets import Pets as pets
from .model.race import Race as race
from .model.race_data import RaceData as race_data
from .model.race_participants import RaceParticipants as race_participants
from .model.race_results import RaceResults as race_results
from .pet_race import PetRace as pet_race

module_logger = logging.getLogger('pet_race_job')
module_logger.setLevel(logging.DEBUG)
ch = logging.StreamHandler()
ch.setLevel(logging.ERROR)
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
ch.setFormatter(formatter)
