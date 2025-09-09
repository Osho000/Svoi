package com.example.Svoi.controllers;

import com.example.Svoi.config.JwtUtil;
import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserPhoto;
import com.example.Svoi.repository.UserRepository;
import com.example.Svoi.service.UserPhotoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private UserPhotoService photoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file,
                                         HttpServletRequest request) {
        try {
            System.out.println("=== UPLOAD START ===");

            // достаём JWT
            String token = jwtUtil.resolveToken(request);
            if (token == null) {
                return ResponseEntity.badRequest().body("No authorization token provided");
            }

            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // сохраняем фото
            UserPhoto photo = photoService.savePhoto(email, file);

            System.out.println("=== UPLOAD SUCCESS ===");
            return ResponseEntity.ok("Photo uploaded successfully with id: " + photo.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}
