kubectl create -f pet-race-namspace.yaml
kubectl create --namespace=pet-races -f pet-race-job-gce.yaml


kubectl create --namespace=pet-races -f dev-pet-race-pod-gce.yaml
kubectl --namespace=pet-races get po
kubectl --namespace=pet-races exec -it py3numpy /bin/sh
kubectl --namespace=pet-races delete po py3numpy

