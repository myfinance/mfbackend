FROM alpine:3.8

RUN apk --update add postgresql-client && rm -rf /var/cache/apk/* && mkdir /var/dumps && chmod 755 /var/dumps

#ARG FILENAME=dumpfilename
ENV FILENAME=dumpfilename

ENTRYPOINT gunzip -c /var/dumps/2018-09-21-13-20.psql.gz | psql postgresql://postgres:vulkan@192.168.100.71:5432/marketdata
#ENTRYPOINT [ "psql" ]
#ENTRYPOINT pg_dump -a postgresql://postgres:vulkan@192.168.100.71:5432/marketdata > /var/dumps/$(date +%Y-%m-%d-%H-%M).psql.gz
