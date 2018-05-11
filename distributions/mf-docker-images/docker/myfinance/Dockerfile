# start from official java container
FROM anapsix/alpine-java:8u144b01_jdk

ADD ./MyFinance /MyFinance
ADD ./dac.res /MyFinance
ADD ./devkeystore.jks /MyFinance/data

ENV DAC_RES_PATH=/MyFinance

WORKDIR /MyFinance/bin/

EXPOSE 1099 44444 8101 8443 8181

ENTRYPOINT ./karaf


