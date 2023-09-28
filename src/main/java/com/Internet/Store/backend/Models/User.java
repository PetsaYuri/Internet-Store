package com.Internet.Store.backend.Models;

import com.Internet.Store.backend.DTO.UserDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    public User() {}

    public User(String name, String email, String encryptedPassword) {
        this.name = name;
        this.email = email;
        password = encryptedPassword;
        permission = "user";
        isBlocked = false;
    }

    public User(String encryptedPassword, UserDTO userDTO) {
        password = encryptedPassword;
        name = userDTO.name();
        email = userDTO.email();
        permission = "user";
        isBlocked = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name, email, permission, password;

    @Column
    private boolean isBlocked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
