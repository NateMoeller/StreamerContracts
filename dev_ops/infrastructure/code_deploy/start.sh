#!/usr/bin/env bash

# Ensure all java instances are dead
killall -9 java

if [[ ${DEPLOYMENT_GROUP_NAME} =~ "Api" ]]
then
    if [[ ${DEPLOYMENT_GROUP_NAME} =~ "Prod" ]]
    then
        applicationSecretsFile = s3://streamer-contracts-application-configurations/prod-application-secrets.yml;
        activeProfile = prod;
    else
        applicationSecretsFile = s3://streamer-contracts-application-configurations/beta-application-secrets.yml;
        activeProfile = beta;
    fi

    echo "Starting StreamerContracts-Api SpringBoot Application"
    sudo cp /var/www/streamercontracts/awslogs_api_$activeProfile.conf /etc/awslogs/awslogs.conf
    sudo chkconfig awslogs on
    currentDate=`date '+%Y_%m_%d_%H'`;
    sudo mkdir /var/log/streamer-contracts
    sudo touch /var/log/streamer-contracts/streamer-contracts.log.$currentDate
    sudo service awslogs restart
    sudo aws s3 cp $applicationSecretsFile /var/www/streamercontracts/application-secrets.yml
    sudo aws s3 cp s3://streamer-contracts-application-configurations/keystore.jks /var/www/streamercontracts/keystore.jks
    java -DSTREAMER_CONTRACTS_KEY_STORE_URL=/var/www/streamercontracts/keystore.jks -Dspring.profiles.active=$activeProfile -Dspring.config.additional-location=file:/var/www/streamercontracts/application-secrets.yml -jar /var/www/streamercontracts/api-1.0-SNAPSHOT.jar > /var/log/streamer-contracts/streamer-contracts.log.$currentDate 2>&1 &
    exit $?
else
    echo "Invalid deployment group name '${DEPLOYMENT_GROUP_NAME}', no role found " 2>&1 &
    exit 1
fi