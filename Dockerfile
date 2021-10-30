FROM openjdk:8-alpine

RUN apk update && apk add bash

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app

COPY target/otif-0.0.1-SNAPSHOT.jar $PROJECT_HOME/otif-0.0.1-SNAPSHOT.jar

WORKDIR $PROJECT_HOME

EXPOSE 8080

CMD ["java","-jar","./otif-0.0.1-SNAPSHOT.jar"]