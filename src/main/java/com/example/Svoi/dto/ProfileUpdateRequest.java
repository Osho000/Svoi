package com.example.Svoi.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String birthCity;
    private String gender;
}
