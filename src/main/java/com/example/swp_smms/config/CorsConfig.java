package com.example.swp_smms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:3001", 
                        "http://localhost:5173",
                        "http://localhost:8080",
                        "http://localhost:4200",
                        "http://192.168.1.54:3000",
                        "http://192.168.1.54:5173"
                )
                .allowedHeaders("*")
                .allowedMethods("*");
    }
} 