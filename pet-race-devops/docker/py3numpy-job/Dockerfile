FROM 10.100.179.231:5000/py3numpy-dev:v1.0
ENV RELEASE 0.1-test
RUN set -ex \
	&& apk add --no-cache --virtual .fetch-deps curl  \
	&& echo ${RELEASE}.tar.gz \
	&& curl -fSL "https://github.com/chrislovecnm/gpmr/archive/v${RELEASE}.tar.gz" -o gpmr.tar.gz \
  && tar xzf gpmr.tar.gz \
  && cd "gpmr-${RELEASE}/pet-race-job/" \
  && python3 setup.py install \
  && cd ../../ \
	&& apk del .fetch-deps \
	&& rm -rf "gpmr-${RELEASE}" ~/.cache
	&& 	find /usr/local -depth \
			\( \
			    \( -type d -a -name test -o -name tests \) \
			    -o \
			    \( -type f -a -name '*.pyc' -o -name '*.pyo' \) \
			\) -exec rm -rf '{}' +

COPY run.sh /
CMD ["/run.sh"]
