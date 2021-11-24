#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/contap
cd $REPOSITORY

APP_NAME=contap
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
#nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
nohup java -jar $JAR_PATH -Dsring.config.location=file:$REPOSITORY/build/libs/application.properties,file:$REPOSITORY/build/libs/email.properties, &
#nohup java -jar $JAR_PATH --spring.config.location=file:$REPOSITORY/build/libs/application.properties &
#nohup java -jar $JAR_PATH --spring.config.location=file:$REPOSITORY/build/libs/application.properties &