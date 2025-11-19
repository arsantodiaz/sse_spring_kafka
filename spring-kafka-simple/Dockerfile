# Stage 1: Build the application using an official Maven image
# This image includes both Maven and JDK 21, removing the need for manual installation.
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml to download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the package
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create the final, minimal image using Distroless
FROM gcr.io/distroless/java21-debian12
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/simple-kafka-0.0.1-SNAPSHOT.jar /app/app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
