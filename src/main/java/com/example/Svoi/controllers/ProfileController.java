package com.example.Svoi.controllers;

import com.example.Svoi.config.JwtUtil;
import com.example.Svoi.dto.ProfileUpdateRequest;
import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserProfile;
import com.example.Svoi.repository.UserProfileRepository;
import com.example.Svoi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile/save")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository profileRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authorization) {
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            String email = jwtUtil.extractEmail(authorization.substring(7));
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserProfile profile = profileRepository.findByUser(user).orElse(null);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }


    @PostMapping
    public ResponseEntity<?> saveProfile(@RequestBody ProfileUpdateRequest request,
                                        @RequestHeader("Authorization") String authorization) {
        System.out.println("=== PROFILE SAVE REQUEST ===");
        System.out.println("Auth header present=" + (authorization != null) + ", startsWithBearer=" + (authorization != null && authorization.startsWith("Bearer ")));
        System.out.println("Request body: " + (request == null ? "<null>" : request.toString()));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("SecurityContext auth class=" + (auth == null ? "<null>" : auth.getClass().getSimpleName())
                + ", principal=" + (auth == null ? "<null>" : String.valueOf(auth.getPrincipal()))
                + ", authorities=" + (auth == null ? "<null>" : String.valueOf(auth.getAuthorities())));
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            if (request == null) {
                return ResponseEntity.badRequest().body("Profile data is required");
            }

            String email = jwtUtil.extractEmail(authorization.substring(7));
            System.out.println("Extracted email from token: " + email);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("Resolved user id=" + user.getId());

            UserProfile profile = profileRepository.findByUser(user).orElse(new UserProfile());
            profile.setUser(user);

            if (request.getFirstName() != null) {
                profile.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                profile.setLastName(request.getLastName());
            }
            if (request.getBirthDate() != null) {
                profile.setBirthDate(request.getBirthDate());
            }
            if (request.getBirthCity() != null) {
                profile.setBirthCity(request.getBirthCity());
            }
            if (request.getGender() != null) {
                profile.setGender(request.getGender());
            }

            profileRepository.save(profile);
            System.out.println("Profile saved for user id=" + user.getId());
            return ResponseEntity.ok("Profile saved successfully");
        } catch (RuntimeException e) {
            System.out.println("RuntimeException saving profile: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

}
