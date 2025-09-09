package com.example.Svoi.service;

import com.example.Svoi.entity.User;
import com.example.Svoi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInterestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterestService interestService;

    public void saveInterests(Long userId, List<String> interests) {
        if (userId == null) throw new RuntimeException("User ID cannot be null");
        if (interests == null || interests.isEmpty()) throw new RuntimeException("Interests list cannot be null or empty");

        // Валидация каждого интереса
        for (String interest : interests) {
            if (!interestService.isValidInterest(interest)) {
                throw new RuntimeException("Invalid interest: " + interest);
            }
        }

        // Сохраняем интересы
        interestService.saveUserInterests(userId, interests);
    }

    public void saveInterestsByIds(Long userId, List<Long> interestIds) {
        if (userId == null) throw new RuntimeException("User ID cannot be null");
        if (interestIds == null || interestIds.isEmpty()) throw new RuntimeException("Interest IDs list cannot be null or empty");

        for (Long id : interestIds) {
            if (id == null) {
                throw new RuntimeException("Found null interest ID in list: " + interestIds);
            }
            if (id <= 0) {
                throw new RuntimeException("Found non-positive interest ID: " + id);
            }
        }

        interestService.saveUserInterestsByIds(userId, interestIds);
    }

    public List<String> getUserInterests(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User ID cannot be null");
        }

        // Проверяем существование пользователя
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        return interestService.getUserInterestNames(userId);
    }
}