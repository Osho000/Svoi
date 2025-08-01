package com.example.Svoi.entity;

import jakarta.persistence.*;

@Entity
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // геттеры и сеттеры
    public Long getId() { return id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
