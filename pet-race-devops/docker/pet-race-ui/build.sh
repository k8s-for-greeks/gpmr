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

rm app.war

docker pull jhipster/jhipster
docker stop jhipster
docker rm jhipster
docker run --name jhipster -d -t jhipster/jhipster
docker exec jhipster /bin/bash -c 'git clone https://github.com/k8s-for-greeks/gpmr.git . && cd pet-race-ui && npm install && ./gradlew build -PskipTest -Pprod -x test -x gulp_test && mv build/libs/*.war ~/app.war'
docker cp jhipster:/home/jhipster/app.war $PWD/
docker stop jhipster
docker rm jhipster

if [ ! -f app.war ]; then
  echo "war not found for build"
  exit 0
fi

docker build -t ${DOCKER} .

echo $DEPLOY
if [ "$DEPLOY" ]; then
  gcloud docker push ${DOCKER}
fi

rm app.war
