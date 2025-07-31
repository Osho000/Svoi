//package com.example.Svoi.model;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.HashSet;
//import java.util.Set;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "users")
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private Integer age;
//    private String gender;
//    private String contact;
//    private String photoUrl;
//    private LocalDate birthdate;
//    private LocalTime birthTime;
//    private String city;
//
//    @ManyToMany
//    @JoinTable(
//            name = "user_interests",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "interest_id")
//    )
//    private Set<Interests> interests = new HashSet<>();
//}
