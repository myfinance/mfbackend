FROM anapsix/alpine-java:8u144b01_jdk

ADD ./database /database

WORKDIR /database

RUN mkdir logs

RUN apk --update add postgresql-client && rm -rf /var/cache/apk/*

ENTRYPOINT ./wrapliquibase "$DB_IMAGE:5432/marketdata" "$DB_USER" "$DB_PW"
