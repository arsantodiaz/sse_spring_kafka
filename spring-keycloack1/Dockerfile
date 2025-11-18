# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final, smaller image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the custom root CA certificate
COPY rootCA.pem /app/rootCA.pem

# Import the custom CA into the Java trust store
# The default password for the cacerts keystore is "changeit"
RUN keytool -import -alias mkcert-root -keystore $JAVA_HOME/lib/security/cacerts -file /app/rootCA.pem -storepass changeit -noprompt

# Copy the application JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
