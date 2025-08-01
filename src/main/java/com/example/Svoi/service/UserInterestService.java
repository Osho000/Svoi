package com.example.Svoi.service;

import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserInterest;
import com.example.Svoi.repository.UserInterestRepository;
import com.example.Svoi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInterestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInterestRepository interestRepository;

    public void saveInterests(Long userId, List<String> interests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        interestRepository.deleteByUser(user);

        for (String interest : interests) {
            UserInterest userInterest = new UserInterest();
            userInterest.setUser(user);
            userInterest.setName(interest);
            interestRepository.save(userInterest);
        }
    }
}
