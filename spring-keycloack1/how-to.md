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
URIs:http://localhost:8090/login/oauth2/code/keycloakIni adalah URL callback 
default yang digunakan oleh Spring Security. 
Sangat penting untuk menuliskannya dengan benar.
•Klik Save.

4.Dapatkan Client Secret:
•Setelah client dibuat, Anda akan diarahkan ke halaman pengaturannya.
•Klik tab Credentials.•Anda akan melihat Client secret. Salin nilai ini.
Anda akan membutuhkannya nanti.