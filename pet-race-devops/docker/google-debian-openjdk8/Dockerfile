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

FROM google/debian:jessie

ADD files /

RUN mv /java.list /etc/apt/sources.list.d/java.list \
  && apt-get update \
  && apt-get -qq -y install --no-install-recommends procps openjdk-8-jre-headless libjemalloc1 curl \
    localepurge \
  && curl -L https://github.com/Yelp/dumb-init/releases/download/v1.0.3/dumb-init_1.0.3_amd64 > /dumb-init \
  && chmod a+rx /dumb-init \
  && apt-get -y purge localepurge curl \
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
