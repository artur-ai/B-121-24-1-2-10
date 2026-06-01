package com.electronic.kafedra.pl.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class JwtCurrentUserService {

    private final JwtService jwtService;

    public JwtCurrentUserService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public Long getCurrentUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        return jwtService.extractUserId(token);
    }
}
