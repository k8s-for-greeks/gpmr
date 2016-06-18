#!/bin/bash

if [[ $DEBUG ]]; then 
  if [[ $(/opt/cassandra/bin/nodetool status | grep $POD_IP) == *"UN"* ]]; then echo "UN"; else echo "NOT UP"; fi
fi
if [[ $(/opt/cassandra/bin/nodetool status | grep $POD_IP) == *"UN"* ]]; then exit 0; else exit 1; fi
