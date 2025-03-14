# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml .
COPY test-config.yml .
COPY src ./src

# Build the application
RUN mvn clean install

# Stage 2: Create the runtime image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the first stage
COPY --from=build /app/target/ajax-alerting-1.0-SNAPSHOT.jar /app/ajax-alerting.jar

# Copy the configuration file to the container
COPY config.yml /app/config.yml

# Expose the port that the application will run on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "ajax-alerting.jar", "server", "config.yml"]