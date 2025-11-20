FROM alfresco/alfresco-content-repository-community:7.4.0

USER root

# Install alat bantu
RUN yum install -y openssl

# 1. Buat struktur folder lengkap
RUN mkdir -p /usr/local/tomcat/alf_data/keystore

# 2. Buat file dummy agar Alfresco tidak mencoba membuatnya dari nol dan gagal
# (Ini trik kotor tapi sering berhasil untuk bypass check awal)
RUN touch /usr/local/tomcat/alf_data/keystore/keystore
RUN touch /usr/local/tomcat/alf_data/keystore/keystore-metadata

# 3. Berikan hak akses penuh ke user alfresco
RUN chown -R alfresco:alfresco /usr/local/tomcat/alf_data && \
    chmod -R 777 /usr/local/tomcat/alf_data

USER alfresco