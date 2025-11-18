package com.example.springkeycloakui;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/secured")
    public String secured(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("username", principal.getPreferredUsername());
        model.addAttribute("claims", principal.getClaims());
        return "secured";
    }

//    @GetMapping("/secured")
//    public String securedPage(@AuthenticationPrincipal OidcUser principal, Model model) {
//        // Mengambil token claims
//        Map<String, Object> claims = principal.getClaims();
//
//        // Mengirim ke HTML
//        model.addAttribute("username", principal.getName());
//        model.addAttribute("claims", claims);
//
//        return "secured";
//    }

}
