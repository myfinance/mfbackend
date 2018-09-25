FROM openjdk:8u171-jre-alpine3.8
ADD ./mfshell /mfshell

RUN apk --update add postgresql-client && rm -rf /var/cache/apk/* && mkdir /var/dumps && chmod 755 /var/dumps && mkdir /mfshell/envconfig

ADD ./dac.res /mfshell/envconfig
ADD ./root /etc/crontabs/root

ENV DAC_RES_PATH=/mfshell/envconfig

CMD chown root:root /etc/crontabs/root && /usr/sbin/crond -f
