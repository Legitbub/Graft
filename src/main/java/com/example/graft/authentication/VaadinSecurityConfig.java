/**
 * This file helps configure the Vaadin framework to
 * bypass some of Spring Security so that the app does
 * not crash. General network security should still be in place.
 */

package com.example.graft.authentication;

import com.example.graft.views.MainView;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class VaadinSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // This permanently stops Spring from appending "?continue" and forcing bad URLs.
        http.requestCache(cache -> cache.disable());

        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            configurer.loginView(MainView.class);
        });

        return http.build();
    }

    //THE FIX: Completely blind Spring Security to Vaadin's background files!
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/images/**",
                "/icons/**",
                "/favicon.ico",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html"
        );
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}