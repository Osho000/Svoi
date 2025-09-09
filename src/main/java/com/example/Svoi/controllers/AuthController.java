package com.example.Svoi.controllers;

import com.example.Svoi.dto.AuthResponse;
import com.example.Svoi.dto.LoginRequest;
import com.example.Svoi.dto.RegisterRequest;
import com.example.Svoi.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("[AuthController] POST /register username='{}' email='{}'", request.getUsername(), request.getEmail());
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("[AuthController] POST /login email='{}'", request.getEmail());
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        log.info("[AuthController] POST /refresh tokenPreview='{}'", refreshToken == null ? "null" : refreshToken.substring(0, Math.min(10, refreshToken.length())) + "..." + (refreshToken == null ? 0 : refreshToken.length()));
        return ResponseEntity.ok(authService.refreshTokens(refreshToken));
    }
}
