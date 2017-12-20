# start from official java container
FROM anapsix/alpine-java:8u144b01_jdk

ADD ./MyFinance /MyFinance
ADD ./devkeystore.jks /MyFinance
ADD ./dac.res /MyFinance

ENV DAC_RES_PATH=/MyFinance

WORKDIR /MyFinance/bin/

EXPOSE 1099 8181 44444 8443 8101

ENTRYPOINT ./karaf


