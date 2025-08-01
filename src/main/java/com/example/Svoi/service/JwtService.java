package com.example.Svoi.service;



import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;



@Service
public class JwtService {

    private final String secret = "your-256-bit-secret-your-256-bit-secret";
    private final Key key = Keys.hmacShaKeyFor("secret-key-must-be-at-least-256-bits-long".getBytes());

    public String getUsernameFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
