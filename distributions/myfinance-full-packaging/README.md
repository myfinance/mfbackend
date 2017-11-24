to create a Docker image:
Docker/mfpostgres(Image with postgres and an initial script to create databsse marketdata):
sudo docker build -t mfpostgres .
docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword

Docker/myfinance