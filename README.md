# Greek Pet Monster Race

## Demo application for [Kubernetes][1] and [Cassandra][2]

This application is built to run Cassandra on Kubernetes at scale.  The demo with Google will run 1000 Cassandra pods on Kubernetes PetSets.

### Kubernetes Features
- PetSet (alpha 1.3)
- Jobs
- Servives
- TBD

### Applications
- Kubernetes (alpha 1.3)
- Cassandra

## Why Greek Pet Monster Race

Well we needed to generate ALOT of data.  We probably cannot generate enough.  Timesequences and randmon data are great use cases for Cassandra.  Kubernetes and Cassandra are both ancient greek words.  We are geeks.  [Ancient pets][4] are cool.

Let's have a RACE!!

## Components

### DevOps
- Dockers
- Yaml for K8s
- Scripts for deploying kubernetes alpha (PetSet is in 1.3 alpha 5 and above)

### Pet Race Job

Python application to generate hopefully a lot of data.  Because it is created to generate data and connections and load and load, it is coded without batch.  Run that inside of a K8s Job, and vroom ... data.

Uses:
- Python 3
- Datastax driver
- [Numpy]

### Pet Race UI

Yah we are K8s guys, but dammit we have to have data vis (btw any UI gurus want to help)?

Uses:
- jhipster
- java / spring
- AngualarJS
- D3

Or leave it empty and use the [link text itself].

URLs and URLs in angle brackets will automatically get turned into links. 
http://www.example.com or <http://www.example.com> and sometimes 
example.com (but not on Github, for example).

Some text to show that the reference links can follow later.

[arbitrary case-insensitive reference text]: https://www.mozilla.org
[1]: http://kubernetes.io/
[2]: http://cassandra.apache.org/
[3]: http://www.numpy.org/
[4]: https://en.wikipedia.org/wiki/List_of_Greek_mythological_creatures
