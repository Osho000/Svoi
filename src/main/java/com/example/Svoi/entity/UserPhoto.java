package com.example.Svoi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table(name = "user_photos")
//public class UserPhoto {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String filename;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getFilename() {
//        return filename;
//    }
//
//    public void setFilename(String filename) {
//        this.filename = filename;
//    }
//
//    public byte[] getData() {
//        return data;
//    }
//
//    public void setData(byte[] data) {
//        this.data = data;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    @Lob
//    @Column(name = "data", columnDefinition = "bytea")
//    @JsonIgnore
//    private byte[] data;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @JsonIgnore
//    private User user;
//
//}
@Entity
@Table(name = "user_photos")
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
}

