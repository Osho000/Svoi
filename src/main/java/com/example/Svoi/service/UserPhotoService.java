package com.example.Svoi.service;

import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserPhoto;
import com.example.Svoi.repository.UserPhotoRepository;
import com.example.Svoi.repository.UserRepository;
import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
public class UserPhotoService {

    @Autowired
    private UserPhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.base-url:http://localhost:8080}") // можно вынести в application.properties
    private String baseUrl;


    public void savePhotos(Long userId, List<byte[]> imageBytesList) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        for (int i = 0; i < imageBytesList.size(); i++) {
            UserPhoto userPhoto = new UserPhoto();
            userPhoto.setFilename("photo_" + System.currentTimeMillis() + "_" + i + ".jpg");
            userPhoto.setData(imageBytesList.get(i));
            userPhoto.setUser(user);

            photoRepository.save(userPhoto);
        }
    }
    public UserPhoto savePhoto(String email, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setFilename(file.getOriginalFilename());
        userPhoto.setData(file.getBytes());
        userPhoto.setUser(user);

        return photoRepository.save(userPhoto); // 🔥 возвращаем объект
    }


    public List<String> getPhotoUrlsByUserId(Long userId) {
        return photoRepository.findByUserId(userId)
                .stream()
                .map(photo -> baseUrl + "/api/user/photo/" + photo.getId())
                .toList();
    }




}
