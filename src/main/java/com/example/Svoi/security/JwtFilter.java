package com.example.Svoi.security;

import com.example.Svoi.config.JwtUtil;
import com.example.Svoi.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String path = request.getRequestURI();
        if (path.startsWith("/api/auth/")) {
            log.trace("Bypassing JWT filter for auth path='{}'", path);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                username = jwtUtil.extractEmail(jwt);
                log.trace("JWT extracted for path='{}' username/email='{}'", request.getRequestURI(), username);
            } else {
                log.debug("No Authorization bearer token for path='{}'", request.getRequestURI());
            }

        } catch (Exception e) {
            log.warn("Invalid JWT token on path='{}' reason='{}'", request.getRequestURI(), e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"unauthorized\",\"path\":\""
                    + request.getRequestURI() + "\",\"message\":\"" + e.getMessage() + "\"}");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userService.loadUserByUsername(username);
                boolean valid = jwtUtil.validateToken(jwt, userDetails);
                if (valid) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentication set for user='{}' authorities='{}' path='{}'",
                            username, userDetails.getAuthorities(), request.getRequestURI());
                } else {
                    log.warn("JWT validation failed for user='{}' path='{}'", username, request.getRequestURI());
                }
            } catch (Exception ex) {
                log.warn("Could not load user by username='{}' path='{}' reason='{}'",
                        username, request.getRequestURI(), ex.getMessage());
            }
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.trace("Proceeding without authentication for path='{}'", request.getRequestURI());
        }
        filterChain.doFilter(request, response);
    }
}
