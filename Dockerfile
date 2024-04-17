#
# Build stage
#
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package -Pproduction

#
# Package stage
#
FROM openjdk:21-jdk-slim
COPY --from=build /usr/src/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
