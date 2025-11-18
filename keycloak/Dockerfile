# Start from the official Keycloak image
FROM quay.io/keycloak/keycloak:24.0.4

# Switch to the root user to copy files and set permissions
USER root

# Copy the local certificates into the image
COPY ./certs/localhost.pem /opt/keycloak/conf/certs/certificate.pem
COPY ./certs/localhost-key.pem /opt/keycloak/conf/certs/key.pem

# Set the correct ownership for the certificate files
RUN chown keycloak:keycloak /opt/keycloak/conf/certs/certificate.pem /opt/keycloak/conf/certs/key.pem

# Switch back to the default non-root user
USER keycloak
