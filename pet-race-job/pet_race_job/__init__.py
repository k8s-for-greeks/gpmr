import logging

ch = logging.StreamHandler()
ch.setLevel(logging.DEBUG)
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
ch.setFormatter(formatter)

module_logger = logging.getLogger('pet_race_job')
module_logger.setLevel(logging.DEBUG)
module_logger.addHandler(ch)

__version_info__ = (1, 0, 0, 'post0')
__version__ = '.'.join(map(str, __version_info__))
