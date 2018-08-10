# start from official java container
FROM anapsix/alpine-java:8u144b01_jdk

ADD ./mfshell /mfshell
RUN mkdir /mfshell/envconfig
ADD ./dac.res /mfshell/envconfig

ENV DAC_RES_PATH=/mfshell/envconfig

WORKDIR /mfshell/


ENTRYPOINT java -jar ./run-job-mfshell.jar
