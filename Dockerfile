# build
FROM gradle:9.5-jdk21-alpine AS build

WORKDIR /usr/app
COPY . .

RUN gradle build --no-daemon

# run
FROM eclipse-temurin:21-jre

ENV JAR_NAME=whois-identifier-1.0.0.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY --from=build $APP_HOME .

SHELL [ "/bin/bash", "-c" ]
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME --ip=$IP
