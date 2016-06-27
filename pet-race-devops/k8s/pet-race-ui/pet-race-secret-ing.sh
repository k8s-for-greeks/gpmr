rm /tmp/nginx.*
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /tmp/nginx.key -out /tmp/nginx.crt -subj "/CN=k8s-for-greeks/O=k8s-for-greeks"
echo "
apiVersion: v1
kind: Secret
metadata:
  name: pet-race-tls
  namespace: pet-race-ui
data:
  tls.crt: `base64 -w 0 /tmp/nginx.crt`
  tls.key: `base64 -w 0 /tmp/nginx.key`
" | /home/clove/gpmr/pet-race-devops/gce/kubernetes/cluster/kubectl.sh create --namespace=pet-race-ui -f -
rm /tmp/nginx.*
