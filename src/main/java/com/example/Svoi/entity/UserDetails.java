package com.example.Svoi.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
public class UserDetails {
    @Id
    private Long id;

    @ElementCollection
    private List<String> interests;


}
