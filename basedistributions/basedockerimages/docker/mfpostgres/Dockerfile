# Use an official Postgres runtime as a parent image
FROM postgres:9.4.15-alpine

# Set the working directory to /docker-entrypoint-initdb.d
WORKDIR /docker-entrypoint-initdb.d

# Copy the current directory contents into the container at /docker-entrypoint-initdb.d
ADD init-user-db.sh /docker-entrypoint-initdb.d

