package com.Internet.Store.backend.Models;

import com.Internet.Store.backend.DTO.ItemDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {

    public Item() {}

    public Item(Long id, String name, String description, String image, int price, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
        listOfWillingUsers = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public Item(Long id, ItemDTO itemDTO, Category category) {
        this.id = id;
        this.name = itemDTO.name();
        this.description = itemDTO.description();
        this.image = itemDTO.image();
        this.price = itemDTO.price();
        this.category = category;
        listOfWillingUsers = new ArrayList<>();
        orders = new ArrayList<>();
    }

    @Id()
    private Long id;

    @Column(nullable = false)
    private String name, description, image;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    private Category category;

    @ManyToMany
    @JsonIgnore
    private List<User> listOfWillingUsers;

    @ManyToMany
    @JsonIgnore
    private List<Order> orders;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<User> getListOfWillingUsers() {
        return listOfWillingUsers;
    }

    public void setListOfWillingUsers(List<User> listOfWillingUsers) {
        this.listOfWillingUsers = listOfWillingUsers;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}