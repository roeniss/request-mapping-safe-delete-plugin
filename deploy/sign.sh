#!/usr/bin/env bash

PROJECT_ROOT=$(git rev-parse --show-toplevel)

PRIVATE_KEY=$(cat "$PROJECT_ROOT/private.pem")
CERTIFICATE_CHAIN=$(cat "$PROJECT_ROOT/chain.crt")

echo -n 'Enter private key password :'
read -s PRIVATE_KEY_PASSWORD
echo ''

./gradlew signPlugin
