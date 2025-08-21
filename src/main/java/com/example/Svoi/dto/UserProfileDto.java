package com.example.Svoi.dto;

import java.util.List;

public class UserProfileDto {
    private String fullName;
    private String age;
    private String email;
    private List<String> photoUrls;
    private List<String> interests;

    // Getters & Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }

}
