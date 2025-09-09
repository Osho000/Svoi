package com.example.Svoi.controllers;

import com.example.Svoi.config.JwtUtil;
import com.example.Svoi.dto.InterestsRequest;
import com.example.Svoi.dto.UserProfileDto;
import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserProfile;
import com.example.Svoi.repository.UserPhotoRepository;
import com.example.Svoi.repository.UserRepository;
import com.example.Svoi.service.InterestService;
import com.example.Svoi.service.UserInterestService;
import com.example.Svoi.service.UserPhotoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserPhotoService userPhotoService;

    @Autowired
    private UserPhotoRepository photoRepository;

    @Autowired
    private InterestService interestService;

    @Autowired
    private UserInterestService userInterestService;

    @Autowired
    private UserRepository userRepository;

    public UserController(InterestService interestService,
                          JwtUtil jwtUtil,
                          UserRepository userRepository) {
        this.interestService = interestService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractEmail(token.substring(7));

            // Используйте JOIN FETCH чтобы избежать N+1 проблемы
            User user = userRepository.findByEmailWithProfile(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Теперь userProfile уже загружен
            if (user.getUserProfile() == null) {
                return ResponseEntity.badRequest().body("User profile not found");
            }

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

    @PostMapping("/interests")
    @Transactional
    public ResponseEntity<?> saveUserInterests(
            @RequestBody InterestsRequest request,
            @RequestHeader("Authorization") String authHeader) {

        try {
            // 1. Проверяем токен
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Invalid token");
            }

            // 2. Извлекаем email из токена
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            log.info("[UserController] Save interests requested by email='{}' idsSize={}",
                    email,
                    request.getInterestIds() == null ? null : request.getInterestIds().size());

            // 3. Находим пользователя
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            log.debug("[UserController] Resolved user id={} email='{}'", user.getId(), user.getEmail());

            // 4. Проверяем входные данные: принимаем только interestIds
            if (request.getInterestIds() == null || request.getInterestIds().isEmpty()) {
                log.warn("[UserController] Empty interestIds payload for user id={}", user.getId());
                return ResponseEntity.badRequest().body("interestIds must be provided and not empty");
            }

            // 5. Сохраняем интересы по ID
            log.debug("[UserController] Saving by IDs: {}", request.getInterestIds());
            userInterestService.saveInterestsByIds(user.getId(), request.getInterestIds());

            return ResponseEntity.ok("Interests saved successfully");

        } catch (Exception e) {
            log.error("[UserController] saveUserInterests failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
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