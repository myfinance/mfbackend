FROM openjdk:8u181-jre

ADD ./mfshell /mfshell
RUN mkdir /mfshell/envconfig
ADD ./dac.res /mfshell/envconfig
ADD ./runner.sh /mfshell

ENV DAC_RES_PATH=/mfshell/envconfig

WORKDIR /mfshell/

#java call has to be wrapped to get arguments from outside the container
ENTRYPOINT ["./runner.sh"]
