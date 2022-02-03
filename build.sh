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

WORKDIR=api
PROPERTIES=src/main/resources/
[ ! -f "$PROPERTIES"/email.properties ] && cp $WORKDIR/email.properties $PROPERTIES/email.properties
[ ! -f "$PROPERTIES"/application.properties ] && cp $WORKDIR/application.properties $PROPERTIES/application.properties
[ ! -f "$PROPERTIES"/cryptography.properties ] && cp $WORKDIR/cryptography.properties $PROPERTIES/cryptography.properties	


./gradlew build || error "Error building application."
sudo docker-compose stop postgres

echo
echo "- Build completed."

echo
echo "= Copying files"

cp $JARFILE $WORKDIR/olistly.jar

echo "- Copy completed"

echo "= Build docker api image"
docker-compose build api || error "Error on build docker api"

echo
echo "Now just enjoy de Olistly using command 'docker-compose up'"

