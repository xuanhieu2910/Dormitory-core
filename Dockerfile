# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/ktx-cds-hust-be-0.0.1-SNAPSHOT.war /app/ktx-cds-hust-be.war


# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/ktx-cds-hust-be.war"]