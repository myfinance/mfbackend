FROM alpine:3.8

RUN apk --update add postgresql-client && rm -rf /var/cache/apk/* && mkdir /var/dumps && chmod 755 /var/dumps

ENTRYPOINT [ "psql" ]
