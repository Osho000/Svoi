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

@Service
public class UserPhotoService {

    @Autowired
    private UserPhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    // Уже есть:
    public void savePhoto(String email, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserPhoto photo = new UserPhoto();
        photo.setFilename(file.getOriginalFilename());
        photo.setData(file.getBytes());
        photo.setUser(user);

        photoRepository.save(photo);
    }

    // Добавь этот метод:
    public void savePhotos(Long userId, List<byte[]> photos) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        for (byte[] photoData : photos) {
            UserPhoto photo = new UserPhoto();
            photo.setUser(user);
            photo.setData(photoData);
            photo.setFilename("photo_" + System.currentTimeMillis()); // можно сделать лучше, например уникальное имя
            photoRepository.save(photo);
        }
    }
}
