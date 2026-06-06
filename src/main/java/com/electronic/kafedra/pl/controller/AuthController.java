package com.electronic.kafedra.pl.controller;

import com.electronic.kafedra.bll.dto.AuthDto;
import com.electronic.kafedra.bll.service.AuthService;
import com.electronic.kafedra.pl.mapper.UiMapper;
import com.electronic.kafedra.pl.model.request.LoginRequest;
import com.electronic.kafedra.pl.model.request.RegisterRequest;
import com.electronic.kafedra.pl.model.response.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UiMapper uiMapper;

    public AuthController(AuthService authService, UiMapper uiMapper) {
        this.authService = authService;
        this.uiMapper = uiMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        AuthDto dto = uiMapper.toDto(request);
        return ResponseEntity.ok(uiMapper.toResponse(authService.login(dto)));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthDto dto = uiMapper.toDto(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uiMapper.toResponse(authService.register(dto)));
    }
}
