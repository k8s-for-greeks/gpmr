rm /tmp/tls.*
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /tmp/tls.key -out /tmp/tls.crt -subj "/CN=k8s-for-greeks/O=k8s-for-greeks"
echo "
apiVersion: v1
kind: Secret
metadata:
  name: tls
data:
  tls.crt: `base64 -w 0 /tmp/tls.crt`
  tls.key: `base64 -w 0 /tmp/tls.key`
" | /home/clove/gpmr/pet-race-devops/gce/kubernetes/cluster/kubectl.sh create --namespace=pet-race-ui -f -
rm /tmp/tls.*
