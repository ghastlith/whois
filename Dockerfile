# build
FROM gradle:9.5-jdk21-alpine AS build

WORKDIR /usr/app
COPY . .

RUN gradle build --no-daemon

# run
FROM eclipse-temurin:21-jre

ENV APP_HOME=/usr/app
ENV JAR_NAME=whois.jar
ENV JAR_PATH=${APP_HOME}/build/libs/${JAR_NAME}

WORKDIR ${APP_HOME}
COPY --from=build ${APP_HOME} .

SHELL [ "/bin/bash", "-c" ]
ENTRYPOINT exec java -jar ${JAR_PATH} --ip=$IP
