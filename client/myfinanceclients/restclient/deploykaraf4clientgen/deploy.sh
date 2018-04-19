#!/bin/bash
echo "start deploy"
docker stack deploy -c ./target/docker-compose-myfinanceclientgen.yml tmyfinance
echo "end deploy"
