package com.example.Svoi.controllers;

import com.example.Svoi.dto.InterestsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService; // или свой сервис

    @PostMapping("/interests")
    public ResponseEntity<?> saveInterests(@RequestBody InterestsRequest request) {
        // Найти пользователя по id и сохранить интересы
        userDetailsService.saveInterests(request.getUserId(), request.getInterests());
        return ResponseEntity.ok("Интересы сохранены");
    }
}
