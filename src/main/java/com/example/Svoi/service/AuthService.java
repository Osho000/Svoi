package com.example.Svoi.service;

import com.example.Svoi.config.JwtUtil;
import com.example.Svoi.dto.AuthResponse;
import com.example.Svoi.dto.LoginRequest;
import com.example.Svoi.dto.RegisterRequest;
import com.example.Svoi.entity.User;
import com.example.Svoi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class); // ✅ Добавьте это!

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        log.info("Register attempt username='{}' email='{}'", request.getUsername(), request.getEmail());

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Пользователь уже существует");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email уже используется");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        String encoded = passwordEncoder.encode(request.getPassword());
        // Логируем только префикс и длину хэша для диагностики
        try {
            String prefix = encoded == null ? "null" : encoded.substring(0, Math.min(4, encoded.length()));
            log.debug("BCrypt produced hash prefix='{}' len={} for email='{}'", prefix, encoded == null ? -1 : encoded.length(), request.getEmail());
        } catch (Exception ignore) { }
        user.setPassword(encoded);
        user.setEmail(request.getEmail());


        User savedUser = userRepository.save(user);
        log.info("User saved id={} email='{}'", savedUser.getId(), savedUser.getEmail());

        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        log.info("Register success email='{}' -> tokens generated", savedUser.getEmail());
        return new AuthResponse(accessToken, refreshToken, savedUser.getId());
    }

    public AuthResponse login(LoginRequest request) {
        log.info("[AuthService] Login attempt email='{}'", request.getEmail());

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        try {
            String hash = user.getPassword();
            String prefix = hash == null ? "null" : hash.substring(0, Math.min(4, hash.length()));
            log.debug("[AuthService] Found user id={} role='{}' hashPrefix='{}' hashLen={}",
                    user.getId(), user.getRole(), prefix, hash == null ? -1 : hash.length());
        } catch (Exception ignore) { }

        try {
            boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
            if (!matches) {
                log.warn("[AuthService] Password mismatch for email='{}'", request.getEmail());
                throw new BadCredentialsException("Bad credentials");
            }
        } catch (IllegalArgumentException e) {
            // Например, если формат хэша в БД некорректный
            log.error("[AuthService] Password encoder error for email='{}' reason='{}'", request.getEmail(), e.getMessage());
            throw e;
        }

        var accessToken = jwtUtil.generateAccessToken(user.getEmail());
        var refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        log.info("[AuthService] Login success email='{}' -> tokens generated", user.getEmail());
        return new AuthResponse(accessToken, refreshToken, user.getId());
    }


    public AuthResponse refreshTokens(String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Refresh token is required");
        }

        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccess = jwtUtil.generateAccessToken(email);
        String newRefresh = jwtUtil.generateRefreshToken(email);
        return new AuthResponse(newAccess, newRefresh, user.getId());
    }
}