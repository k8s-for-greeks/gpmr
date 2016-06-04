#!/bin/bash
# Spins up a 1k node cluster on gce. Run this from a kubernetes directory.
usage() {
  echo "Usage: $0 [-p]" 1>&2;
  echo "  -p PROJECT_ID"
  exit 1;
}

if [ $? != 0 ] ; then usage ; fi

while getopts "p:h" o; do
    case "${o}" in
        p)
         PROJECT_ID=${OPTARG}
         ;;
        h)
            usage
            ;;
        *)
            usage
            ;;
    esac
done
shift $((OPTIND-1))
if [ -z "$PROJECT_ID" ]; then
	usage
fi
# I used the latest alpha build uncomment below to pull
command -v gcloud  >/dev/null 2>&1 || { echo "I require gcloud but it's not installed.  Aborting." >&2; exit 1; }
G_P=$(gcloud config list)

if [[ $G_P == *"${PROJECT_ID}"* ]]
then
  echo "$PROJECT_ID selected"
else
  echo "gcloud not set to $PROJECT_ID"
  echo "run 'gcloud config set project $PROJECT_ID'"
  exit 1
fi

echo $G_P
read -p "Is this the config you want to used? y/n" -n 1 -r
echo    # (optional) move to a new line
if [[ ! $REPLY =~ ^[Yy]$ ]]
then
    echo "BYE"
    exit 1
fi

if [ ! -f "$PWD/kubernetes/cluster/kube-up.sh" ]; then
    echo "kubernete not found downloading"
    # todo sholud we test for this??
    rm kubernetes.tar.gz
    wget https://github.com/kubernetes/kubernetes/releases/download/v1.3.0-alpha.5/kubernetes.tar.gz
    tar xzf kubernetes.tar.gz
    rm kubernetes.tar.gz
fi
cd kubernetes

# These numbers were adjusted from the 2k node jenkins test
# (https://github.com/kubernetes/test-infra/blob/master/jenkins/job-configs/kubernetes-jenkins/kubernetes-e2e.yaml#L688)
export NUM_NODES="500"
export MASTER_SIZE="n1-standard-32"
export NODE_SIZE="n1-standard-8"
export KUBE_GCE_ZONE="us-central1-c"
# This is to avoid full cluster turnup failing if a handful of nodes aren't healthy
# within default timeout.
export ALLOWED_NOTREADY_NODES="10"
# spin up cluster
echo "Starting cluster"
echo Nodes: $NUM_NODES
echo Master size: $MASTER_SIZE
echo Node size: $NODE_SIZE
echo Zone: $KUBE_GCE_ZONE 

cluster/kube-up.sh

# when done with cluster
# cluster/kube-down.sh

# To share access, create a standalone kubeconfig file
cluster/kubectl.sh config view --minify --flatten > kubeconfig.yml
