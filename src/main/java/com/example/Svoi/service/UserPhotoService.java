package com.example.Svoi.service;

import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserPhoto;
import com.example.Svoi.repository.UserPhotoRepository;
import com.example.Svoi.repository.UserRepository;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class UserPhotoService {

    @Autowired
    private UserPhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    public void savePhoto(String email, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserPhoto photo = new UserPhoto();
        photo.setFilename(file.getOriginalFilename());
        photo.setData(file.getBytes());
        photo.setUser(user);

        photoRepository.save(photo);
    }

    public void savePhotos(Long userId, List<byte[]> photos) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (byte[] photoData : photos) {
            UserPhoto photo = new UserPhoto();
            photo.setUser(user);
            photo.setData(photoData);
            photo.setFilename("photo_" + UUID.randomUUID().toString());
            photoRepository.save(photo);
        }
    }
}
