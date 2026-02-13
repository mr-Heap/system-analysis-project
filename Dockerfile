FROM gradle:8.7-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

FROM amazoncorretto:21-alpine-jdk

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*SNAPSHOT.jar /app/spring-boot-application.jar

ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]
