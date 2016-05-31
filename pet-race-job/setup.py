# -*- coding: utf-8 -*-

from distutils.core import setup

with open('README.md') as f:
    readme = f.read()

with open('LICENSE') as f:
    lic = f.read()

setup(
    name='pet-race-job',
    version='0.0.1',
    description='Pet Race Job for kubernetes demo',
    long_description=readme,
    author='Chris Love',
    author_email='chris.love at apollobit.com',
    url='https://github.com/k8s-for-greeks/gpmr/tree/master/pet-race-job',
    license=lic,
    packages=['pet_race_job', 'pet_race_job.model'],
    scripts=['pet-race-job', 'data-importer']
)
