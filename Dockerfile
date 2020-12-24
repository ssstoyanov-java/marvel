FROM openjdk:11-jdk-slim
EXPOSE 8080 8080
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} marvel.jar
ENTRYPOINT ["java","-jar","/marvel.jar"]