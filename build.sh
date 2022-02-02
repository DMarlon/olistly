#!/bin/bash
function error {
    echo
    echo "# $1"
    exit -1
}

echo "= Starting docker postgres"
if [ -z `command -v docker` ]; then
	error "First need to up docker postgres"
fi

sudo docker-compose build postgres || error "Error on build docker postgres"
sudo docker-compose up -d postgres || error "Error on start docker postgres"

echo "= Build API Olistly"

WORKDIR=api
if [ ! -d "$WORKDIR" ]; then
	mkdir $WORKDIR
fi

JARFILE=build/libs/olistly-0.0.1-OLY.jar
[ -f "$JARFILE" ] && rm "$JARFILE"

./gradlew build || error "Error building application."
sudo docker-compose stop postgres

echo
echo "- Build completed."

echo
echo "= Copying files"

PROPERTIES=src/main/resources/
cp $JARFILE $WORKDIR/olistly.jar
[ ! -f "$WORKDIR"/email.properties ] && cp $PROPERTIES/email-exemple.properties $WORKDIR/email.properties
[ ! -f "$WORKDIR"/application.properties ] && cp $PROPERTIES/application-exemple.properties $WORKDIR/application.properties
[ ! -f "$WORKDIR"/cryptography.properties ] && cp $PROPERTIES/cryptography-exemple.properties $WORKDIR/cryptography.properties	

echo "- Copy completed"
echo
echo "Now open api folder and configure the properties files application, email and cryptography and then just enjoy de Olistly using command 'docker-compose up'"

