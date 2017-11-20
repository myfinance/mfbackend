#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE marketdata;
    GRANT ALL PRIVILEGES ON DATABASE marketdata TO postgres;
EOSQL