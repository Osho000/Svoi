package com.example.Svoi.repository;

import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    void deleteByUser(User user);
}
