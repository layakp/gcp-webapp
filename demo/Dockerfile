# base image with jdk 17
# FROM amazoncorretto:17.0.7-alpine

#FROM alpine/java:22-jdk
FROM alpine/java:22-jre

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY target/demo-0.0.1-SNAPSHOT.jar /app

# Command to run the Spring Boot application
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
