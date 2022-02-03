#!/bin/bash
function error {
    echo
    echo "# $1"
    exit -1
}

echo "= Starting docker postgres"
if [ -z `command -v docker` ]; then
	error "Docker is necessary"
fi

docker-compose up -d postgres || error "Error on start docker postgres"

echo "= Build API Olistly"

JARFILE=build/libs/olistly-0.0.1-OLY.jar
[ -f "$JARFILE" ] && rm "$JARFILE"

./gradlew build || error "Error building application."
sudo docker-compose stop postgres

echo
echo "- Build completed."

echo
echo "= Copying files"

WORKDIR=api
cp $JARFILE $WORKDIR/olistly.jar

echo "- Copy completed"

echo "= Build docker api image"
docker-compose build api || error "Error on build docker api"

echo
echo "Now just enjoy de Olistly using command 'docker-compose up'"

