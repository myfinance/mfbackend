#!/bin/bash
# mvn clean install
docker image build -t holgerfischer/myfinance-mfpostgres:0.0.0-alpha.1 ./distributions/mf-docker-images/docker/mfpostgres/
docker image build -t holgerfischer/myfinance-mfdbupdate:0.0.0-alpha.1 ./distributions/mf-docker-images/target/docker-prep/mfdb/
#docker image build -t holgerfischer/myfinance-mfbackend:0.0.0-alpha.1 ./distributions/mf-docker-images/target/docker-prep/myfinance/
kubectl apply -f deploy.yaml