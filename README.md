# Greek Pet Monster Race

## Demo application for [Kubernetes][1] and [Cassandra][2]

This application is built to run Cassandra on Kubernetes at scale, hosted on Google Cloud.  The demo with Google will run 1000 Cassandra pods on Kubernetes using a new feature in 1.3 called PetSets.

### Kubernetes Features
- PetSet (alpha 1.3)
- Jobs
- Services
- Resource Controllers
- Secrets
- Volumes, PVC / PV
- GCE Integration
- Muitple Zones (Ubernetes)
- DNS

### Applications
- Cassandra
- Python Batch Jobs
- JHipster Front End

## Why Greek Pet Monster Race

Well we needed to generate ALOT of data.  We probably cannot generate enough.  Timesequences and randmon data are great use cases for Cassandra.  Kubernetes and Cassandra are both ancient greek words.  We are geeks.  [Ancient pets][4] are cool.

Let's have a RACE!!

## Components

### DevOps
- Dockers
- Yaml for K8s
- Scripts for deploying kubernetes on GCE in mulitple AZ (PetSet is in 1.3 alpha 5 and above)

### Pet Race Job

Python application to generate hopefully a lot of data.  Because it is created to generate data and connections and load and load, it is coded without batch.  Run that inside of a K8s Job, and vroom ... data.

Uses:
- Python 3
- Datastax driver
- [Numpy][3]

### Pet Race UI

Yah we are K8s guys, but dammit we have to have data vis (btw any UI gurus want to help)?

Uses:
- [jhipster][4]
- java / spring
- AngualarJS
- D3

[1]: http://kubernetes.io/
[2]: http://cassandra.apache.org/
[3]: http://www.numpy.org/
[4]: https://en.wikipedia.org/wiki/List_of_Greek_mythological_creatures
[5]: https://jhipster.github.io/
