# start from official java container
FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine

ADD ./MyFinance /MyFinance
RUN mkdir /MyFinance/envconfig
ADD ./dac.res /MyFinance/envconfig
ADD ./devkeystore.jks /MyFinance/envconfig

ENV DAC_RES_PATH=/MyFinance/envconfig

WORKDIR /MyFinance/bin/

EXPOSE 1099 44444 8101 8443 8181

ENTRYPOINT ./karaf


