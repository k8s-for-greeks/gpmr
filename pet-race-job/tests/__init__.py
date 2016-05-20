import logging
import sys
import socket

log = logging.getLogger()
log.setLevel('DEBUG')

# if nose didn't already attach a log handler, add one here
if not log.handlers:
    handler = logging.StreamHandler()
    handler.setFormatter(logging.Formatter('%(asctime)s %(levelname)s [%(module)s:%(lineno)s]: %(message)s'))
    log.addHandler(handler)