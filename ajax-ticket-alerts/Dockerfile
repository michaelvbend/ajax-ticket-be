# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the target directory to the container
COPY target/ajax-alerting-1.0-SNAPSHOT.jar /app/ajax-alerting.jar

# Copy the configuration file to the container
COPY config.yml /app/config.yml

# Expose the port that the application will run on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "ajax-alerting.jar", "server", "config.yml"]