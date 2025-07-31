package com.example.Svoi.controllers;


import com.example.Svoi.dto.ProfileUpdateRequest;
import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserProfile;
import com.example.Svoi.repository.UserProfileRepository;
import com.example.Svoi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository profileRepository;

    // 🔹 Получить профиль
    @GetMapping
    public ResponseEntity<UserProfile> getProfile(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        UserProfile profile = profileRepository.findByUser(user).orElse(null);
        return ResponseEntity.ok(profile);
    }

    // 🔹 Обновить или создать профиль
    @PostMapping
    public ResponseEntity<?> saveProfile(@RequestBody ProfileUpdateRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        UserProfile profile = profileRepository.findByUser(user).orElse(new UserProfile());
        profile.setUser(user); // важно для новых записей

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setBirthDate(request.getBirthDate());
        profile.setBirthCity(request.getBirthCity());
        profile.setGender(request.getGender());

        profileRepository.save(profile);
        return ResponseEntity.ok("Профиль успешно сохранён");
    }
}
