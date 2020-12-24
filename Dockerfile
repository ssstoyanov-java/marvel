FROM openjdk:11-jdk-slim

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /marvel.jar

ENTRYPOINT ["java","-jar","/marvel.jar"]