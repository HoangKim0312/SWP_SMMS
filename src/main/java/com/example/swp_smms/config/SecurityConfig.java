package com.example.swp_smms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api/auth/**",
                    "/api/account/**",
                    "/api/role/**",
                    "/api/class/**",
                    "/api/medical-profile/**",
                    "/api/health-event/**",
                    "/api/health-event-follow-up/**",
                    "/api/event-medication-usage/**",
                    "/api/medication/**",
                    "/api/medication-sent/**",
                    "/api/student-parent/**",
                    "/api/vaccine/**",
                    "/api/vaccine-batch/**",
                    "/api/vaccination-record/**",
                    "/api/vaccination-notice/**",
                    "/api/vaccination-confirmation/**",
                    "/api/health-check-record/**",
                    "/api/health-check-notice/**",
                    "/api/health-check-confirmation/**"
                ).permitAll()
                .anyRequest().permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 