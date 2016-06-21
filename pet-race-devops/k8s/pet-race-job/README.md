
```console
# namspace
kubectl create -f pet-race-namspace.yaml

# race
kubectl create --namespace=pet-races -f pet-race-job.yaml

# pod
kubectl create --namespace=pet-races -f pet-race-pod-gce.yaml
kubectl get --namespace=pet-races po
kubectl exec -it --namespace=pet-races py3numpy -- /bin/sh
kubectl delete --namespace=pet-races -f pet-race-pod-gce.yaml

```
