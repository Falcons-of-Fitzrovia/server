FROM ubuntu:latest
LABEL authors="Samindu"

ENTRYPOINT ["top", "-b"]

# Use an official OpenJDK runtime as a parent image
FROM adoptopenjdk:17-jdk-hotspot

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the POM file
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Specify the command to run your application
CMD ["java", "-jar", "target/your-application.jar"]
