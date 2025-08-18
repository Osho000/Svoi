package com.example.Svoi.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
public class MatchResultDTO {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthCity;
    private String photoUrl;
    private double matchPercentage;
    private int commonInterests;
    private java.util.List<String> sharedInterests;

    public MatchResultDTO(Long userId, String username, String firstName, String lastName, String gender, String birthCity, String photoUrl, double matchPercentage, int commonInterests, List<String> sharedInterests) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthCity = birthCity;
        this.photoUrl = photoUrl;
        this.matchPercentage = matchPercentage;
        this.commonInterests = commonInterests;
        this.sharedInterests = sharedInterests;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getCommonInterests() {
        return commonInterests;
    }

    public void setCommonInterests(int commonInterests) {
        this.commonInterests = commonInterests;
    }

    public List<String> getSharedInterests() {
        return sharedInterests;
    }

    public void setSharedInterests(List<String> sharedInterests) {
        this.sharedInterests = sharedInterests;
    }
}
