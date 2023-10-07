package com.Internet.Store.backend.Models;

import com.Internet.Store.backend.DTO.UserDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    public User() {}

    public User(String name, String email, String encryptedPassword) {
        this.name = name;
        this.email = email;
        password = encryptedPassword;
        permission = "user";
        isBlocked = false;
        basket = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public User(String encryptedPassword, UserDTO userDTO) {
        password = encryptedPassword;
        name = userDTO.name();
        email = userDTO.email();
        permission = "user";
        isBlocked = false;
        basket = new ArrayList<>();
        orders = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name, email, permission, password;

    @Column
    private boolean isBlocked;

    @ManyToMany
    private List<Item> basket;

    @OneToMany
    private List<Order> orders;

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

    public List<Item> getBasket() {
        return basket;
    }

    public void setBasket(List<Item> basket) {
        this.basket = basket;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}