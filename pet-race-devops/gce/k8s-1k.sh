#!/bin/bash
# Spins up a 1k node cluster on gce. Run this from a kubernetes directory.

#
# TODO:
# - allow tweaking of Node disks
# - which node distro??
#
KUBE_RELEASE=v1.3.0-beta.1
KUBE_ROOT=kubernetes

export NUM_NODES=${NUM_NODES:-334}
export NUM_NODES_2=${NUM_NODES_2:-333}
export NUM_NODES_3=${NUM_NODES_3:-333}
export MULTIZONE=${MULTIZONE:0}

export MASTER_SIZE=${MASTER_SIZE:-n1-standard-32}
export NODE_SIZE=${NODE_SIZE:-n1-standard-4}
export KUBE_GCE_ZONE=${KUBE_GCE_ZONE:-us-central1-b}
export KUBE_GCE_ZONE_2=${KUBE_GCE_ZONE_2:-us-central1-f}
export KUBE_GCE_ZONE_3=${KUBE_GCE_ZONE_3:-us-central1-c}
#export KUBE_ENABLE_CLUSTER_MONITORING=google
#export MASTER_DISK_SIZE=${MASTER_DISK_SIZE:-40GB}
#export NODE_DISK_TYPE=${NODE_DISK_TYPE:-local-ssd}
#export NODE_DISK_SIZE=${NODE_DISK_SIZE:-100GB}
export KUBE_OS_DISTRIBUTION=${KUBE_OS_DISTRIBUTION:-gci}

# This is to avoid full cluster turnup failing if a handful of nodes aren't healthy
# within default timeout.
export ALLOWED_NOTREADY_NODES=10

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
command -v gcloud  >/dev/null 2>&1 || { echo "I require gcloud but it's not installed.  https://cloud.google.com/shell/docs/starting-cloud-shell" >&2; exit 1; }

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

if [ ! -f "$PWD/$KUBE_ROOT/cluster/kube-up.sh" ]; then
    echo "kubernetes not found downloading $KUBE_RELEASE"
    # todo sholud we test for this file??
    gsutil cp gs://kubernetes-release/release/$KUBE_RELEASE/kubernetes.tar.gz .
    tar xzf kubernetes.tar.gz

    # Detect the OS name/arch so that we can find our binary
    case "$(uname -s)" in
      Darwin)
      host_os=darwin
      ;;
      Linux)
      host_os=linux
      ;;
      *)
      echo "Unsupported host OS.  Must be Linux or Mac OS X." >&2
      exit 1
      ;;
    esac
    case "$(uname -m)" in
      x86_64*)
      host_arch=amd64
      ;;
      i?86_64*)
      host_arch=amd64
      ;;
      amd64*)
      host_arch=amd64
      ;;
      arm*)
      host_arch=arm
      ;;
      i?86*)
      host_arch=386
      ;;
      s390x*)
      host_arch=s390x
      ;;
      ppc64le*)
      host_arch=ppc64le
      ;;
      *)
      echo "Unsupported host arch. Must be x86_64, 386, arm, s390x or ppc64le." >&2
      exit 1
      ;;
    esac
    echo $host_os
    echo $host_arch
    echo "downloading kubectl $KUBE_RELEASE"
    gsutil cp gs://kubernetes-release/release/$KUBE_RELEASE/kubernetes-client-$host_os-$host_arch.tar.gz .
    tar xzf kubernetes-client-$host_os-$host_arch.tar.gz

    KUBECTL=${KUBE_ROOT}/platforms/${host_os}/${host_arch}/
    mkdir -p $KUBECTL
    cp ${KUBE_ROOT}/client/bin/kubectl $KUBECTL
fi

cd $KUBE_ROOT

echo "Starting cluster"
echo Nodes: $NUM_NODES
echo Master size: $MASTER_SIZE
echo Node size: $NODE_SIZE
echo Zone 1: $KUBE_GCE_ZONE

cluster/kube-up.sh
# To share access, create a standalone kubeconfig file
cluster/kubectl.sh config view --minify --flatten > $HOME/kubeconfig.yml

if [[ $MULTIZONE == 1 ]]; then
  echo Zone 2: $KUBE_GCE_ZONE_2
  export KUBE_MASTER=kubernetes-master
  export KUBE_USE_EXISTING_MASTER=true 
  export KUBE_GCE_ZONE=${KUBE_GCE_ZONE_2} 
  export NUM_NODES=${NUM_NODES_2} 
  cluster/kube-up.sh
  echo Zone 3: $KUBE_GCE_ZONE_3
  export KUBE_GCE_ZONE=${KUBE_GCE_ZONE_3} 
  export NUM_NODES=${NUM_NODES_3} 
  cluster/kube-up.sh
fi

# when done with cluster
# KUBE_USE_EXISTING_MASTER=true KUBE_GCE_ZONE=us-central1-f cluster/kube-down.sh &
# KUBE_USE_EXISTING_MASTER=true KUBE_GCE_ZONE=us-central1-c cluster/kube-down.sh &
# Wait for the other zones to go down
# KUBE_GCE_ZONE=us-central1-b cluster/kube-down.sh
