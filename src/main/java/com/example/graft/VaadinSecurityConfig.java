/**
 * This file helps configure the Vaadin framework to
 * bypass some of Spring Security so that the app does
 * not crash. General network security should still be in place.
 */

package com.example.graft;

import com.example.graft.views.MainView;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class VaadinSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            configurer.loginView(MainView.class);
        });

        http.formLogin(form -> form
                .loginPage("/")               // Forces the 403 redirect to go to the root
                .loginProcessingUrl("/login") // Matches loginForm.setAction("login")
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}