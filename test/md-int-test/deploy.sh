#!/bin/bash
echo "start deploy"
docker stack deploy -c ./target/docker-compose-integrationtest.yml imyfinance
echo "end deploy"
