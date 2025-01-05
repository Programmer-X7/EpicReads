# Use the official OpenJDK image as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot JAR file from the target folder to the container
COPY target/epicreads-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your Spring Boot application will run on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]