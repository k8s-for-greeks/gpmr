# Setup Process

## Get K8s 1.3 running locally
You need a beta or 1.3 GA.  Use a tool like kube-solo or k8s vagrant from coreos.

## Local Registry
For development we run most of the dockers out of a local registry on k8s.  We currently have issues with docker dependancies and the fact that a of the dockers are currenly in gce.  We will correct that eventually.  At this point you would need to change the ip addresses in the local build scripts, and the yaml files, which applicable.

TBD
