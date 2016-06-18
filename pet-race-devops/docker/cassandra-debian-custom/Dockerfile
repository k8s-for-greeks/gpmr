# Copyright 2016 The Kubernetes Authors All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM gcr.io/aronchick-apollobit/google-debian-openjdk8:v1.0

ENV CASS_VERSION=3.7-SNAPSHOT CASS_DIR=/opt/cassandra PATH=$PATH:/opt/cassandra/bin

ADD files /

RUN apt-get update \
  && apt-get -qq -y install --no-install-recommends curl python-minimal \
    localepurge \
  && curl "https://storage.googleapis.com/kubernetes-cassandra/apache-cassandra-${CASS_VERSION}-bin.tar.gz" | tar xz \
  && mv apache-cassandra-${CASS_VERSION} ${CASS_DIR} \
  && find /opt/cassandra/lib/sigar-bin/ -type f | grep -v ibsigar-x86-linux.so | xargs rm \
  && rm -rf  ${CASS_DIR}/lib/licenses ${CASS_DIR}/javadoc \
  && chmod a+rx /run.sh /ready-probe.sh \
  && mkdir -p /cassandra_data/data \
  && mv /logback.xml /cassandra.yaml $CASS_DIR/conf/ \
  && mv /kubernetes-cassandra.jar ${CASS_DIR}/lib/ \
  && apt-get -y purge curl localepurge \
  && apt-get clean \
  && rm -rf \
        doc \
        man \
        info \
        locale \
        /var/lib/apt/lists/* \
        /var/log/* \
        /var/cache/debconf/* \
        common-licenses \
        ~/.bashrc \
        /etc/systemd \
        /lib/lsb \
        /lib/udev \
        /usr/share/doc/ \
        /usr/share/doc-base/ \
        /usr/share/man/ \
        /tmp/*

VOLUME ["/cassandra_data/data"]

# 7000: intra-node communication
# 7001: TLS intra-node communication
# 7199: JMX
# 9042: CQL
# 9160: thrift service not included cause it is going away
EXPOSE 7000 7001 7199 9042

# Not able to do this until https://github.com/kubernetes/kubernetes/issues/2630 is resolved
# if you are using attached storage
# USER cassandra

CMD ["/dumb-init", "/bin/bash", "/run.sh"]
