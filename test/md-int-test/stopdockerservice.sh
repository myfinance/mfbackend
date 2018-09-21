#!/bin/bash
echo "remove imyfinance-service"
docker stack rm imyfinance
echo "remove volume"
sleep 30
docker container prune -f
docker volume rm imyfinance_imyfinancedata 
