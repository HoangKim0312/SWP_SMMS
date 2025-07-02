package com.example.swp_smms.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String path = request.getRequestURI();
        if (path.contains("/api/v1/auth/login")) {
            response.getWriter().write("{\"message\": \"Invalid email or password\"}");
        } else {
            response.getWriter().write("{\"message\": \"You need to log in to access this resource\"}");
        }
    }
} 