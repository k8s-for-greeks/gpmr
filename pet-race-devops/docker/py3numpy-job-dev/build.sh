#!/bin/bash
VERSION=v1.0

usage() {
  echo "Usage: $0 [-p]" 1>&2;
  echo "  -p PROJECT_ID -d"
  exit 1;
}

if [ $? != 0 ] ; then usage ; fi

while getopts "p:hd" o; do
    case "${o}" in
        p)
         PROJECT_ID=${OPTARG}
         ;;
        d)
         DEPLOY=true
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

REPO=gcr.io/$PROJECT_ID
DOCKER="${REPO}/${PWD##*/}:${VERSION}"

docker build --no-cache -t ${DOCKER} .

echo $DEPLOY
if [ "$DEPLOY" ]; then
  gcloud docker push ${DOCKER}
fi
