# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final, smaller image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Install iputils-ping (for ping) and telnet for debugging
RUN apt-get update && \
    apt-get install -y iputils-ping telnet ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# --- Tambahkan bagian ini untuk sertifikat CA ---
# Salin sertifikat root CA mkcert dari host ke dalam container
# Pastikan 'rootCA.pem' ada di direktori yang sama dengan Dockerfile Anda
COPY rootCA.pem /usr/local/share/ca-certificates/rootCA.crt

# Perbarui trust store sertifikat sistem
RUN update-ca-certificates
# --- Akhir bagian tambahan ---

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]