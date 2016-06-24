FROM gcr.io/aronchick-apollobit/py3numpy:v1.0
ENV RELEASE intial-app-deploy
RUN set -ex \
  && apk add --no-cache --virtual .fetch-deps curl  \
  && curl -fSL "https://github.com/chrislovecnm/gpmr/archive/${RELEASE}.tar.gz" -o gpmr.tar.gz \
  && tar xzf gpmr.tar.gz \
  && cd "gpmr-${RELEASE}/pet-race-job/" \
  && python3 setup.py install \
  && mv data / \
  && apk del .fetch-deps \
  && cd / \
  && rm gpmr.tar.gz \
  && rm -rf ~/.cache gpmr-${RELEASE} \
  && find /usr/local -depth \
		\( \
		    \( -type d -a -name test -o -name tests \) \
		    -o \
		    \( -type f -a -name '*.pyc' -o -name '*.pyo' \) \
		\) -exec rm -rf '{}' +
COPY run.sh /
CMD ["/run.sh"]
