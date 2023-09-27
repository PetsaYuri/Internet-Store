package com.Internet.Store.backend.Models;

import com.Internet.Store.backend.DTO.ItemDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
@Embeddable
public class Item {

    public Item() {}

    public Item(Long id, String name, String description, String image, int price, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
    }

    public Item(Long id, ItemDTO itemDTO, Category category) {
        this.id = id;
        this.name = itemDTO.name();
        this.description = itemDTO.description();
        this.image = itemDTO.image();
        this.price = itemDTO.price();
        this.category = category;
    }

    @Id()
    private Long id;

    @Column(nullable = false)
    private String name, description, image;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JsonBackReference
    private Category category;

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
}
