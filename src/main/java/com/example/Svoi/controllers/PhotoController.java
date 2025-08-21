package com.example.Svoi.controllers;

import com.example.Svoi.config.JwtUtil;
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
    private JwtUtil jwtUtil;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file,
                                         HttpServletRequest request) {
        try {
            System.out.println("=== UPLOAD START ===");

            String token = jwtUtil.resolveToken(request);
            System.out.println("Token: " + token);

            if (token == null) {
                System.out.println("No token provided");
                return ResponseEntity.badRequest().body("No authorization token provided");
            }

            String email = jwtUtil.extractEmail(token);
            System.out.println("Email from token: " + email);


            if (file == null) {
                System.out.println("File is null");
                return ResponseEntity.badRequest().body("File is null");
            }

            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize());
            System.out.println("File content type: " + file.getContentType());

            if (file.isEmpty()) {
                System.out.println("File is empty");
                return ResponseEntity.badRequest().body("File is empty");
            }

            photoService.savePhoto(email, file);
            System.out.println("=== UPLOAD SUCCESS ===");
            return ResponseEntity.ok("Photo uploaded successfully");

        } catch (Exception e) {
            System.out.println("=== UPLOAD ERROR ===");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }



}
