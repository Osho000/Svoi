package com.example.Svoi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;

    // Конструктор ДОЛЖЕН устанавливать все поля!
    public AuthResponse(String accessToken, String refreshToken, Long userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId; // ← Это обязательно!
    }

    // Геттеры и сеттеры
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}