package com.example.Svoi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Добавленные методы
    public String getRole() {
        return this.roles != null && !this.roles.isEmpty()
                ? this.roles.iterator().next().getName()
                : null;
    }

    public void setRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        this.roles = Set.of(role);
    }
}