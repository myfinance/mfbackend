# start from official java container
FROM anapsix/alpine-java:8u144b01_jdk

ADD ./MyFinance /MyFinance
ADD ./mfshell /MfShell
RUN mkdir /MyFinance/envconfig
ADD ./dac.res /MyFinance/envconfig
ADD ./devkeystore.jks /MyFinance/envconfig
ADD crontab /etc/cron.d/hello-cron
RUN chmod 0644 /etc/cron.d/hello-cron
RUN crontab /etc/cron.d/hello-cron

ENV DAC_RES_PATH=/MyFinance/envconfig

WORKDIR /MyFinance/bin/

EXPOSE 1099 44444 8101 8443 8181

ENTRYPOINT ./karaf


