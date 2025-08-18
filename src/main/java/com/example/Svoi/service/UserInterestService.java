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
        if (interests == null) {
            throw new RuntimeException("Interests list cannot be null");
        }
        for (String interest : interests) {
            if (!interestService.isValidInterest(interest)) {
                throw new RuntimeException("Invalid interest: " + interest);
            }
        }
        interestService.saveUserInterests(userId, interests);
    }

    public void saveInterestsByIds(Long userId, List<Long> interestIds) {
        if (interestIds == null) {
            throw new RuntimeException("Interest IDs list cannot be null");
        }
        interestService.saveUserInterestsByIds(userId, interestIds);
    }

    public List<String> getUserInterests(Long userId) {
        return interestService.getUserInterestNames(userId);
    }
}
