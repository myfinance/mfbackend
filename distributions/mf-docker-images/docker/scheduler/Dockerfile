FROM alpine:3.8

RUN apk --update add postgresql-client && rm -rf /var/cache/apk/* && mkdir /var/dumps && chmod 755 /var/dumps

ADD ./root /etc/crontabs/root

CMD chown root:root /etc/crontabs/root && /usr/sbin/crond -f
