package com.example.Svoi.repository;

import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    List<UserPhoto> findByUser(User user);
}
