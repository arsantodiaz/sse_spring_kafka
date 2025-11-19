package com.example.springkeycloakui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login**", "/error**").permitAll() // Halaman public
                        .anyRequest().authenticated() // Halaman lain butuh login
                )
                .oauth2Login(Customizer.withDefaults()) // Config login standar
                .logout(logout -> logout
                        // INI BAGIAN PENTINGNYA:
                        // Gunakan handler khusus OIDC agar redirect ke Keycloak saat logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                // ⬇️ TAMBAHKAN BAGIAN INI ⬇️
                .headers(headers -> headers
                        .cacheControl(cache -> cache.disable()) // Memaksa browser tidak menyimpan cache
                );

        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        // Setelah logout dari Keycloak, user akan dilempar kembali ke halaman utama aplikasi ("/")
        // {baseUrl} akan diganti otomatis dengan http://nb631.local:8090
        successHandler.setPostLogoutRedirectUri("{baseUrl}/");

        return successHandler;
    }
}