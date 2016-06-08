# clove @ instance-1 in ~/gpmr/pet-race-devops/k8s/cassandra on git:k8-cluster x [8:10:59]
$ ../../gce/kubernetes/cluster/kubectl.sh exec cassandra-100 -- nodetool status | grep UN | wc && ../../gce/kubernetes/cluster/kubectl.sh get po | wc && ../../gce/kubernetes/cluster/kubectl.sh exec cassandra-0 -- nodetool info | grep Uptime
    260    2080   27040
    261    1305   13092
Uptime (seconds)       : 12082
