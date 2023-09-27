package com.Internet.Store.backend.Models;

import com.Internet.Store.backend.DTO.CategoryDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    public Category() {}

    public Category(String title, String description) {
        this.title = title;
        this.description = description;
        this.items = new ArrayList<>();
    }

    public Category(CategoryDTO categoryDTO) {
        this.title = categoryDTO.title();
        this.description = categoryDTO.description();
        this.items = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title, description;

    @OneToMany
    @JsonManagedReference
    private List<Item> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
