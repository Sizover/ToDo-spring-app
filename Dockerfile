# To build
#   docker build . -t kiap/kiap-backend-reducers
# To run (all services on localhost)
#   docker run --env FLUENTD_HOST=localhost --env FLUENTD_PORT=24224 --env DB_USER=kiap --env DB_PASSWORD=1234 --env DB_HOST=localhost:5432 --env KAFKA_SERVERS=localhost:9092 --name kiap-backend-reducers --network host -i --rm kiap/kiap-backend-reducers

# Stage 1: create gradle cache
ARG GRADLE_IMAGE=gradle:7.5-jdk11
FROM ${GRADLE_IMAGE} as gradle-cache
ENV BUILD_SRC /usr/src/kiap/
ENV GRADLE_USER_HOME /home/gradle/cache/
RUN mkdir -p ${BUILD_SRC}
RUN mkdir -p ${GRADLE_USER_HOME}
COPY gradle ${BUILD_SRC}gradle
COPY config ${BUILD_SRC}config
COPY gradlew build.gradle.kts settings.gradle.kts gradle.properties ${BUILD_SRC}
WORKDIR ${BUILD_SRC}
RUN gradle build -i --build-cache || return 0 && echo "DO NOT PANIC! Error is ok at 1st stage"

# Stage 2: build
FROM gradle-cache as builder
ENV BUILD_SRC /usr/src/kiap/
COPY . ${BUILD_SRC}
WORKDIR ${BUILD_SRC}
RUN gradle bootJar -i --stacktrace --build-cache

# Stage 3: run
FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
USER root
WORKDIR /opt/kiap
COPY --from=builder /usr/src/kiap/build/libs/kiap-backend*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

