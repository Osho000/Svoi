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
            String token = jwtUtil.resolveToken(request);
            if (token == null) {
                return ResponseEntity.badRequest().body("No authorization token provided");
            }

            String email = jwtUtil.extractUsername(token);
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize());
            System.out.println("Email: " + email);

            photoService.savePhoto(email, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        }  catch (Exception e) {
        e.printStackTrace(); // Покажет точную причину в консоли Spring
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }

}
}
