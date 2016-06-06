FROM alpine:edge

RUN apk --update add openjdk8

ENV VERSION 3.5

RUN set -ex \
  && apk add --no-cache --virtual .fetch-deps curl gnupg perl \
  && adduser -S cassandra \
  && mkdir -p /opt \
	&& curl "http://apache.cs.utah.edu/cassandra/${VERSION}/apache-cassandra-${VERSION}-bin.tar.gz" | tar xz \
  && mv apache-cassandra-${VERSION} /opt/cassandra

ADD kubernetes-cassandra.jar /opt/cassandra/lib/kubernetes-cassandra.jar
ADD logback.xml /opt/cassandra/logback.xml
ADD cassandra.yaml /opt/cassandra/conf/cassandra.yaml
ADD run.sh /run.sh
RUN chmod +x /run.sh
RUN mkdir -p /cassandra_data/data; chown -R cassandra: /cassandra_data
RUN chown -R cassandra: /opt

CMD /run.sh
