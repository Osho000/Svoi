package com.example.Svoi.controllers;

import com.example.Svoi.dto.ProfileUpdateRequest;
import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserProfile;
import com.example.Svoi.repository.UserProfileRepository;
import com.example.Svoi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile/save")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository profileRepository;

    @GetMapping
    public ResponseEntity<?> getProfile(Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            User user = userRepository.findByEmail(principal.getName())
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
    public ResponseEntity<?> saveProfile(@RequestBody ProfileUpdateRequest request, Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            if (request == null) {
                return ResponseEntity.badRequest().body("Profile data is required");
            }

            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

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
            return ResponseEntity.ok("Profile saved successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

}
