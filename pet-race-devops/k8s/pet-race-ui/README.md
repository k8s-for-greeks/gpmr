kubectl create -f pet-race-ui-namespace.yaml
./pet-race-secret.sh
kubectl create --namespace=pet-race-ui -f pet-race-ui.yaml
