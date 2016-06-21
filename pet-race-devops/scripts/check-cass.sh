#!/bin/bash
PATH=$PATH:/home/clove/gpmr/pet-race-devops/gce/kubernetes/cluster/

usage() {
  echo "Usage: $0 [-l] [-c]" 1>&2;
  echo "-l prints logs of last instance"
  echo "-c prints count of pod and results of up Cassandra nodes"
  exit 1;
}

while getopts "lc" o; do
    case "${o}" in
        l)
         LOGS=true
         ;;
        c)
         CASS=true
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

PO=$(kubectl.sh get po -o wide | grep cass)

SORTED=$( echo "${PO}" | sort -n -r -k 1.10)
NUM_PO=$(echo "{$PO}" | wc -l)
PETSET=$(kubectl.sh get petset -o wide)

echo "Pods up"
echo "$SORTED"
echo "=============================="
echo "cass pods up:       $NUM_PO"
echo "=============================="
echo "Petsets"
echo "${PETSET}"

RESTART=$(echo "$SORTED" | grep -v -e "Running   0")
if [ -n "$RESTARTED" ]; then
  echo "restarted"
  echo "$RESTART"
fi

if [ -n "$CASS" ]; then
  EXEC=$(kubectl.sh exec -it cassandra-2 -- nodetool status | grep UN | wc -l)
  echo "=============================="
  echo "CASSANDRA INFO"
  echo "cass pods up:       $NUM_PO"
  echo "nodetool status UN: $EXEC"
fi

if [ -n "$LOGS" ]; then
  LAST=$(echo "${SORTED}" | tail -n 1 | awk '{ print $1 }')
  LAST_LOGS=$(kubectl.sh logs $LAST)
  echo "=============================="
  echo "$LAST_LOGS"
fi
