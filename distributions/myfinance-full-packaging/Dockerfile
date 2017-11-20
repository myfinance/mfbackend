# Use an official Postgres runtime as a parent image
#FROM postgres:9.4.15-alpine
FROM cafapi/java-postgres

# Set proxy server, replace host:port with values for your servers
ENV http_proxy http://xn01598:XN01598@proxy.dzbank.vrnet:8080
ENV https_proxy https://xn01598:XN01598@proxy.dzbank.vrnet:8080

# Set the working directory to /docker-entrypoint-initdb.d
WORKDIR /docker-entrypoint-initdb.d

# Copy the current directory contents into the container at /docker-entrypoint-initdb.d
ADD init-user-db.sh /docker-entrypoint-initdb.d

#add java
#RUN apk --update add openjdk8-jre

#ADD ./java /usr/java/default/
#ENV JAVA_HOME=/usr/java/default
#ENV PATH=$JAVA_HOME/bin:$PATH:${PATH}


#WORKDIR /usr/java/default/bin
#run chmod 775 ./java
#RUN pwd
#RUN ls
#ADD ./HelloWorld.class /test
#RUN echo $PATH

ADD ./MyFinance /MyFinance
#RUN /MyFinance/database/dac_liquibase localhost:5432/marketdata postgres vulkan


#RUN java -version

ENTRYPOINT /MyFinance/database/dac_liquibase localhost:5432/marketdata postgres vulkan