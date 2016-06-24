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

PO_DATA=$(kubectl.sh get po -o wide | grep cassandra-data)
PO_ANA=$(kubectl.sh get po -o wide | grep cassandra-ana)

SORTED_DATA=$( echo "${PO_DATA}" | sort -n -r -k 1.15)
NUM_PO_DATA=$(echo "{$PO_DATA}" | wc -l)

SORTED_ANA=$( echo "${PO_ANA}" | sort -n -r -k 1.20)
NUM_PO_ANA=$(echo "{$PO_ANA}" | wc -l)

PETSET=$(kubectl.sh get petset -o wide)
TOTAL=$((NUM_PO_DATA + NUM_PO_ANA))

echo "Pods up"
echo "$SORTED_DATA"
echo "$SORTED_ANA"
echo "Data:      $NUM_PO_DATA"
echo "Analytics: $NUM_PO_ANA"
echo "Total:     $TOTAL"
echo "=============================="
echo "Petsets"
echo "${PETSET}"

#RESTART=$(echo "$SORTED" | grep -v -e "Running   0")
#if [ -n "$RESTARTED" ]; then
#  echo "restarted"
#  echo "$RESTART"
#fi

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
