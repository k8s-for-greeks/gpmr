#!/bin/bash

if [[ $DEBUG ]]; then 
  if [[ $(nodetool status | grep $POD_IP) == *"UN"* ]]; then echo "UN"; else echo "NOT UP"; fi
fi
if [[ $(nodetool status | grep $POD_IP) == *"UN"* ]]; then exit 0; else exit 1; fi
