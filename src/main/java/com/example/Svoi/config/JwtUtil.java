package com.example.Svoi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final long accessTokenExpiresIn = 60000 * 15; // 15 минут
    private final long refreshTokenExpiresIn = 60000 * 60 * 24; // 24 часа

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String username) {
        String token = generateToken(username, accessTokenExpiresIn);
        log.debug("Generated access token for user='{}' expInMs={} tokenPreview='{}'",
                username, accessTokenExpiresIn, previewToken(token));
        return token;
    }

    public String generateRefreshToken(String username) {
        String token = generateToken(username, refreshTokenExpiresIn);
        log.debug("Generated refresh token for user='{}' expInMs={} tokenPreview='{}'",
                username, refreshTokenExpiresIn, previewToken(token));
        return token;
    }

    private String generateToken(String username, long tokenExpiresIn) {
        long expMillis = System.currentTimeMillis() + tokenExpiresIn;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String subject = claims.getSubject();
        log.debug("Extracted username='{}' from tokenPreview='{}' exp='{}'",
                subject, previewToken(token), claims.getExpiration());
        return subject;
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            log.trace("Token valid tokenPreview='{}'", previewToken(token));
            return true;
        } catch (JwtException e) {
            log.warn("Token invalid tokenPreview='{}' reason='{}'", previewToken(token), e.getMessage());
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.trace("Resolved token from header tokenPreview='{}' path='{}'", previewToken(token), request.getRequestURI());
            return token;
        }
        log.trace("No bearer token on path='{}'", request.getRequestURI());
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            Date expiration = claims.getExpiration();

            boolean valid = (username.equals(userDetails.getUsername()) && !expiration.before(new Date()));
            log.debug("Validated token userMatches={} username='{}' tokenPreview='{}' exp='{}'",
                    valid, username, previewToken(token), expiration);
            return valid;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token validation failed tokenPreview='{}' reason='{}'",
                    previewToken(token), e.getMessage());
            return false;
        }
    }

    private String previewToken(String token) {
        if (token == null) return "null";
        int len = token.length();
        int show = Math.min(10, len);
        return token.substring(0, show) + "..." + len;
    }
}
