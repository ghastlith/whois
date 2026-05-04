# build
FROM gradle:8.10.1-jdk21 AS build

WORKDIR /usr/app
COPY . .

RUN gradle build

# run
FROM openjdk:21

ENV JAR_NAME=whois-identifier-1.0.0.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY --from=build $APP_HOME .

# run
SHELL [ "/bin/bash", "-c" ]
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME --ip=$IP
