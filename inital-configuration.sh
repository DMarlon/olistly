#!/bin/bash
function error {
    echo
    echo "# $1"
    exit -1
}

echo "= Starting initial configuration"
if [ -z `command -v docker` ]; then
	error "Docker is necessery"
fi

echo "= Build docker postgres image"

sudo docker-compose build postgres || error "Error on build docker postgres"

echo "= Create api folder"

WORKDIR=api
if [ ! -d "$WORKDIR" ]; then
	mkdir $WORKDIR
fi
echo "- api folder created"

echo
echo "= Copying files"

PROPERTIES=src/main/resources/
[ ! -f "$WORKDIR"/email.properties ] && cp $PROPERTIES/email-exemple.properties $WORKDIR/email.properties
[ ! -f "$WORKDIR"/application.properties ] && cp $PROPERTIES/application-exemple.properties $WORKDIR/application.properties
[ ! -f "$WORKDIR"/cryptography.properties ] && cp $PROPERTIES/cryptography-exemple.properties $WORKDIR/cryptography.properties	

echo "- Copy completed"
echo
echo "Now open api folder and configure the properties files application, after configuration run build.sh"

