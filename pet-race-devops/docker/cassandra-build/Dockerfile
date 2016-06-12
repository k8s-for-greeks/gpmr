# Copyright 2017
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

FROM gcr.io/aronchick-apollobit/openjdk:v1.0

ENV ANT_VERSION=1.9.7 ANT_HOME=/apache-ant-1.9.7 PATH=${PATH}:/apache-ant-1.9.7/bin

RUN set -ex \
  && apk add --no-cache --virtual .fetch-deps curl tar gzip \
  && curl -L http://archive.apache.org/dist/ant/binaries/apache-ant-${ANT_VERSION}-bin.tar.gz > ant.tar.gz \
  && tar -xzf ant.tar.gz \
  && rm ant.tar.gz \
  && apk del .fetch-deps \
  && rm -rf /var/cache/apk/*
