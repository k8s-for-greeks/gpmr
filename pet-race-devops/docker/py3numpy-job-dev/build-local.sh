#!/bin/bash
VERSION=v1.0
REPO=10.100.179.231:5000
DOCKER="${REPO}/py3numpy-job:${VERSION}"

docker build -t ${DOCKER} .
docker push ${DOCKER}
