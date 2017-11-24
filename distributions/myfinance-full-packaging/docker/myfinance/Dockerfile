# Use an official Postgres runtime as a parent image
FROM anapsix/alpine-java:8u144b01_jdk

# Set proxy server, replace host:port with values for your servers
ENV http_proxy http://xn01598:XN01598@proxy.dzbank.vrnet:8080
ENV https_proxy https://xn01598:XN01598@proxy.dzbank.vrnet:8080

#add java
#RUN apk --update add openjdk8-jre

ADD ./MyFinance /MyFinance
WORKDIR /MyFinance/database
RUN ./dac_liquibase 172.17.0.1:5432/marketdata postgres vulkan

#RUN java -version  

