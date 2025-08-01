package com.example.Svoi.controllers;

import com.example.Svoi.dto.InterestsRequest;
import com.example.Svoi.entity.User;
import com.example.Svoi.repository.UserRepository;
import com.example.Svoi.service.JwtService;
import com.example.Svoi.service.UserDetailsService;
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
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserPhotoService userPhotoService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/interests")
    public ResponseEntity<?> saveInterests(@RequestBody InterestsRequest request) {
        userDetailsService.saveInterests(request.getUserId(), request.getInterests());
        return ResponseEntity.ok("Интересы сохранены");
    }

    @PostMapping("/upload-photos")
    public ResponseEntity<?> uploadPhotos(@RequestParam("images") List<MultipartFile> files,
                                          @RequestHeader("Authorization") String token) {
        try {
            String email = jwtService.getUsernameFromToken(token.substring(7));
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            Long userId = user.getId();
            List<byte[]> imageBytes = new ArrayList<>();

            for (MultipartFile file : files) {
                imageBytes.add(file.getBytes());
            }

            userPhotoService.savePhotos(userId, imageBytes);
            return ResponseEntity.ok("Фотографии сохранены");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка загрузки изображений");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        }
    }
}
