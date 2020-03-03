FROM alpine:3.8

RUN apk --update add postgresql-client && rm -rf /var/cache/apk/* && mkdir /var/dumps && chmod 755 /var/dumps && apk --update add gzip

ENV FILENAME=dumpfilename
ENV USER=postgres
ENV PASSWORD=password
ENV URL=mfdb
ENV PORT=5432

ENTRYPOINT gunzip -c /var/dumps/$FILENAME | psql postgresql://$USER:$PASSWORD@$URL:$PORT/marketdata
