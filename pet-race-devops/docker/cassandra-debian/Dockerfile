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

ADD files /

ENV CASSANDRA_AUTO_BOOTSTRAP=false

RUN mv /cassandra.list /etc/apt/sources.list.d/cassandra.list \
  && gpg --keyserver pgp.mit.edu --recv-keys F758CE318D77295D \
  && gpg --export --armor F758CE318D77295D | apt-key add - \
  && gpg --keyserver pgp.mit.edu --recv-keys 2B5C1B00 \
  && gpg --export --armor 2B5C1B00 | apt-key add - \
  && gpg --keyserver pgp.mit.edu --recv-keys 0353B12C \
  && gpg --export --armor 0353B12C | apt-key add - \
  && apt-get update \
  && apt-get -qq -y install --no-install-recommends curl cassandra localepurge \
  && chmod a+rx /run.sh /dumb-init /ready-probe.sh \
  && mkdir -p /cassandra_data/data \
  && mv /logback.xml /cassandra.yaml /etc/cassandra/ \
  && find /usr/share/cassandra/lib/sigar-bin -type f | grep -v libsigar-x86-linux.so | xargs rm \

  # Not able to run as cassandra until https://github.com/kubernetes/kubernetes/issues/2630 is resolved
  # && chown -R cassandra: /etc/cassandra /cassandra_data /run.sh /kubernetes-cassandra.jar \
  # && chmod o+w -R /etc/cassandra /cassandra_data \

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
