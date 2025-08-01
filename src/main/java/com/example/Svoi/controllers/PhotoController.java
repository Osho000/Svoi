package com.example.Svoi.controllers;

import com.example.Svoi.service.UserPhotoService;
import com.example.Svoi.util.JwtUtil;
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
            String email = jwtUtil.extractUsername(token);
            photoService.savePhoto(email, file);
            return ResponseEntity.ok("Фото успешно загружено");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }
}
