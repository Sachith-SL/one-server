# Use Docker BuildKit syntax version 1 for improved build features
# syntax=docker/dockerfile:1

# ==================== Stage 1: Build ====================

# Use Eclipse Temurin JDK 21 as the base image for building the application
FROM eclipse-temurin:21-jdk AS build

# Set the working directory inside the container to /workspace
WORKDIR /workspace

# Copy Gradle wrapper scripts and build config files into the container
COPY gradlew gradlew.bat build.gradle settings.gradle ./

# Copy the Gradle wrapper directory (contains the wrapper jar and properties)
COPY gradle ./gradle

# Make the Gradle wrapper script executable
RUN chmod +x gradlew

# Copy the application source code into the container
COPY src ./src

# Build the Spring Boot fat JAR, skipping tests to speed up the build
RUN ./gradlew bootJar -x test

# ==================== Stage 2: Run ====================

# Use a lighter JRE-only image for running the application (no compiler needed)
FROM eclipse-temurin:21-jre

# Set the working directory inside the container to /app
WORKDIR /app

# Create a non-root system group and user named "app" for security
RUN addgroup --system app && adduser --system --ingroup app app

# Copy the built JAR from the build stage into the runtime image
COPY --from=build /workspace/build/libs/*.jar /app/app.jar

# Inform Docker that the application listens on port 8080
EXPOSE 8080

# Switch to the non-root "app" user to run the application securely
USER app

# Define the command to run the Spring Boot application when the container starts
ENTRYPOINT ["java","-jar","/app/app.jar"]
