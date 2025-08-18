package com.example.Svoi.controllers;

import com.example.Svoi.config.JwtUtil;
import com.example.Svoi.dto.InterestsRequest;
import com.example.Svoi.entity.User;
import com.example.Svoi.repository.UserRepository;
import com.example.Svoi.service.UserInterestService;
import com.example.Svoi.service.UserPhotoService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserInterestService userInterestService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserPhotoService userPhotoService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/interests")

    public ResponseEntity<?> saveInterests(@RequestBody InterestsRequest request) {
        try {
            if (request.getInterestIds() != null && !request.getInterestIds().isEmpty()) {
                userInterestService.saveInterestsByIds(request.getUserId(), request.getInterestIds());
            } else if (request.getInterests() != null && !request.getInterests().isEmpty()) {
                userInterestService.saveInterests(request.getUserId(), request.getInterests());
            } else {
                return ResponseEntity.badRequest().body("Either interestIds or interests must be provided");
            }
            return ResponseEntity.ok("Interests saved successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PostMapping("/upload-photos")
    public ResponseEntity<?> uploadPhotos(@RequestParam("images") List<MultipartFile> files,
                                          @RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long userId = user.getId();
            List<byte[]> imageBytes = new ArrayList<>();

            for (MultipartFile file : files) {
                imageBytes.add(file.getBytes());
            }

            // ВАЖНО: вызываем savePhotos, а не savePhoto
            userPhotoService.savePhotos(userId, imageBytes);

            return ResponseEntity.ok("Photos saved successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading images");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}
