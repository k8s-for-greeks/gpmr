#!/bin/bash

usage() {
  echo "Usage: $0 [-m]" 1>&2;
  echo "  -m Monster "
  exit 1;
}

if [ $? != 0 ] ; then usage ; fi
while getopts "m:p:c:s:" o; do
    case "${o}" in
        m)
         MONSTER=${OPTARG}
         ;;
        p)
         PAR=${OPTARG}
         ;;
        c)
         COMP=${OPTARG}
         ;;
        s)
         SCALE=${OPTARG}
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

if [ -z "$MONSTER" ]; then
  usage
fi

if [ -z "$PAR" ]; then
  PAR=2
fi

if [ -z "$COMP" ]; then
  COMP=4
fi

if [ -z "$SCALE" ]; then
  SCALE=3
fi

DT=$(date --utc '+%Y/%m/%d %H:%M:%SZ')

read -d '' YAML << EOF
apiVersion: batch/v1
kind: Job
metadata:
  name: pet-race-${MONSTER,,}
  labels:
    name: pet-races
spec:
  parallelism: ${PAR}
  completions: ${COMP}
  template:
    metadata:
      name: pet-race-${MONSTER,,}
      labels:
        name: pet-races
    spec:
      containers:
      - name: pet-race-${MONSTER,,}
        image: gcr.io/aronchick-apollobit/py3numpy-job:v1.1
        command: ["pet-race-job", "--description='A Pet Race of ${MONSTER} ${DT}'", "--length=100", "--pet=${MONSTER}", "--scale=${SCALE}"]
        resources:
          limits:
            cpu: "2"
          requests:
            cpu: "2"
      restartPolicy: Never
EOF

echo "Starting"
echo "======"
echo "$YAML"

echo "$YAML" | /home/clove/gpmr/pet-race-devops/gce/kubernetes/cluster/kubectl.sh create --namespace=pet-race-ui -f -
