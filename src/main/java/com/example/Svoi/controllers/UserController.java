package com.example.Svoi.controllers;

import com.example.Svoi.config.JwtUtil;
import com.example.Svoi.dto.InterestsRequest;
import com.example.Svoi.dto.UserProfileDto;
import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserProfile;
import com.example.Svoi.repository.UserPhotoRepository;
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
    private UserPhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            // Достаём email из JWT
            String email = jwtUtil.extractEmail(token.substring(7));

            // Находим пользователя
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserProfile profile = user.getUserProfile();
            if (profile == null) {
                return ResponseEntity.badRequest().body("User profile not found");
            }

            // Собираем DTO
            UserProfileDto dto = new UserProfileDto();
            dto.setFullName(user.getUserProfile().getFirstName() + " " + user.getUserProfile().getLastName());
            dto.setAge(String.valueOf(UserProfile.calculateAge(user.getUserProfile().getBirthDate())));
            dto.setEmail(user.getEmail());
            dto.setPhotoUrls(userPhotoService.getPhotoUrlsByUserId(user.getId()));
            dto.setInterests(userInterestService.getUserInterests(user.getId()));

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    // ======= Сохранение интересов =======
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

    // ======= Загрузка фото =======
    @PostMapping("/upload-photos")
    public ResponseEntity<?> uploadPhotos(@RequestParam("images") List<MultipartFile> files,
                                          @RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractEmail(token.substring(7));
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long userId = user.getId();
            List<byte[]> imageBytes = new ArrayList<>();

            for (MultipartFile file : files) {
                imageBytes.add(file.getBytes());
            }

            userPhotoService.savePhotos(userId, imageBytes);

            return ResponseEntity.ok("Photos saved successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading images");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/photo/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long photoId) {
        return photoRepository.findById(photoId)
                .map(photo -> ResponseEntity.ok()
                        .header("Content-Type", "image/jpeg")
                        .body(photo.getData()))
                .orElse(ResponseEntity.notFound().build());
    }


}
