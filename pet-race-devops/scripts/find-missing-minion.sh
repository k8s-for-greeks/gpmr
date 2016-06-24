#1/bin/bash
kubectl get nodes | grep -v master | grep -v NAME | awk '{ print $1 }' > ~/nodes.txt
gcloud compute instances list | grep minion | awk '{ print $1 }' | sort > ~/gce-instances.txt
diff ~/nodes.txt ~/gce-instances.txt
