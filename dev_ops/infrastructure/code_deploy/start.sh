#!/usr/bin/env bash

# Ensure all java instances are dead
killall -9 java

if [[ ${DEPLOYMENT_GROUP_NAME} =~ "WebServer" ]]
then
    echo "Stopping nginx"
    service nginx stop
    sudo cp /var/www/streamercontracts/nginx.conf /etc/nginx/nginx-beta.conf
    echo "Starting nginx StreamerContracts-WebServer."
    sudo service nginx start
elif [[ ${DEPLOYMENT_GROUP_NAME} =~ "Api" ]]
then
    echo "Starting StreamerContracts-Api SpringBoot Application"
    java -Dspring.profiles.active=beta -jar /var/www/streamercontracts/api-1.0-SNAPSHOT.jar > /dev/null 2>&1 &
    exit $?
else
    echo "Invalid deployment group name '${DEPLOYMENT_GROUP_NAME}', no role found " 2>&1 &
    exit 1
fi