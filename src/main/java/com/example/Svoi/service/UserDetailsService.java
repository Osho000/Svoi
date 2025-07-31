package com.example.Svoi.service;

import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserInterest;
import com.example.Svoi.repository.UserInterestRepository;
import com.example.Svoi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInterestRepository interestRepository;

    public void saveInterests(Long userId, List<String> interests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Удалить старые интересы
        interestRepository.deleteByUser(user);

        // Сохранить новые
        for (String interest : interests) {
            UserInterest userInterest = new UserInterest();
            userInterest.setUser(user);
            userInterest.setName(interest);
            interestRepository.save(userInterest);
        }
    }
}
