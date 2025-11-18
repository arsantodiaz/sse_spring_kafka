Administration Console dengan username admin dan password admin.

1.Buat Realm Baru:
•Arahkan kursor ke tulisan "master" di pojok kiri atas, lalu klik Create Realm.
•Masukkan Realm name: my-app-realm.
•Klik Create.

2.Buat Client Baru:
•Pastikan Anda berada di dalam realm my-app-realm.
•Klik Clients di menu sebelah kiri, lalu klik Create client.
•Isi form berikut:
•Client type: Biarkan OpenID Connect.
•Client ID: spring-ui-client (ini penting, catat nama ini).
•Klik Next.

3.Konfigurasi Client:
•Pada layar berikutnya, aktifkan Client authentication (ubah dari OFF ke ON). Ini penting untuk keamanan.
•Biarkan sisanya default, lalu klik Next.
•Di bagian Login settings, tambahkan URL berikut ke Valid redirect
URIs:http://localhost:8090/login/oauth2/code/keycloak
Ini adalah URL callback 
default yang digunakan oleh Spring Security. 
Sangat penting untuk menuliskannya dengan benar.
•Klik Save.

4.Dapatkan Client Secret:
•Setelah client dibuat, Anda akan diarahkan ke halaman pengaturannya.
•Klik tab Credentials.
•Anda akan melihat Client secret.
•Salin nilai ini. Anda akan membutuhkannya nanti.


New-NetFirewallRule -DisplayName "Docker Keycloak Port 6565" -Direction Inbound -LocalPort 6565 -Protocol TCP -Action Allow



Langkah 1: Menemukan file rootCA.pem di mesin host Andamkcert menyimpan sertifikat root CA-nya di lokasi khusus. Untuk menemukan lokasi pastinya:1.Buka Command Prompt (CMD) atau PowerShell di mesin Windows Anda.2.Jalankan perintah ini:Shell Scriptmkcert -CAROOTPerintah ini akan menampilkan path lengkap ke direktori tempat mkcert menyimpan sertifikat root CA-nya. Contoh output mungkin terlihat seperti: C:\Users\YourUsername\AppData\Local\mkcert3.Buka File Explorer dan navigasikan ke direktori yang ditampilkan oleh perintah mkcert -CAROOT.4.Di dalam direktori tersebut, Anda akan menemukan file bernama rootCA.pem (atau mungkin rootCA.crt). Ini adalah file yang kita butuhkan.Langkah 2: Menyalin file rootCA.pem ke direktori spring-simpleSetelah Anda menemukan rootCA.pem:1.Salin file rootCA.pem tersebut.2.Tempelkan file tersebut ke direktori proyek spring-simple Anda: spring-simplePastikan nama file yang Anda tempelkan adalah rootCA.pem.


# curl -v https://host.docker.internal:6565/realms/my-app-realm/.well-known/openid-configuration


docker exec -it keycloak /opt/keycloak/bin/kcadm.sh config credentials --server http://localhost:8080 --realm master --user admin --password admin
docker exec -it keycloak /opt/keycloak/bin/kcadm.sh update realms/master -s sslRequired=NONE
docker exec -it keycloak /opt/keycloak/bin/kcadm.sh update realms/my-app-realm -s sslRequired=NONE



