package com.bookworms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String passwordHash;

    @Column(length = 1000)
    private String bio = "";

    @Column(nullable = false)
    private Integer avatarColorValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_", nullable = false)
    private Role role = Role.READER;

    @Column(nullable = false)
    private boolean banned = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public User() {}

    public User(String name, String email, String passwordHash, Integer avatarColorValue) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.avatarColorValue = avatarColorValue;
    }

    // --- getters & setters ---
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Integer getAvatarColorValue() { return avatarColorValue; }
    public void setAvatarColorValue(Integer avatarColorValue) { this.avatarColorValue = avatarColorValue; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isBanned() { return banned; }
    public void setBanned(boolean banned) { this.banned = banned; }

    public Instant getCreatedAt() { return createdAt; }
}
