#!/bin/sh
export EXTERNAL_IP=$(docker-machine ip)
exec docker-compose $@
