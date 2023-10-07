package com.Internet.Store.backend.Models;

import com.Internet.Store.backend.DTO.OrderDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {

    public Order() {}

    public Order(Long id, String phone, String address, int price_of_delivery, int amount_of_order, User customer) {
        this.id = id;
        this.phone = phone;
        this.address = address;
        this.price_of_delivery = price_of_delivery;
        this.amount_of_order = amount_of_order;
        this.customer = customer;
        state = "new";
        items = customer.getBasket();
    }

    public Order(Long id, OrderDTO orderDTO, User customer, Item item) {
        this.id = id;
        phone = orderDTO.phone();
        address = orderDTO.address();
        price_of_delivery = orderDTO.price_of_delivery();
        amount_of_order = item.getPrice() + price_of_delivery;
        this.customer = customer;
        state = "new";
        items = new ArrayList<>();
        items.add(item);
    }

    public Order(Long id, OrderDTO orderDTO, int amount_of_order, User customer) {
        this.id = id;
        phone = orderDTO.phone();
        address = orderDTO.address();
        price_of_delivery = orderDTO.price_of_delivery();
        this.amount_of_order = amount_of_order;
        this.customer = customer;
        state = "new";
        items = customer.getBasket();
    }

    @Id
    private Long id;

    @Column(nullable = false)
    private String state, phone, address;

    @Column(nullable = false)
    private int price_of_delivery, amount_of_order;

    @ManyToOne
    private User customer;

    @ManyToMany
    private List<Item> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice_of_delivery() {
        return price_of_delivery;
    }

    public void setPrice_of_delivery(int price_of_delivery) {
        this.price_of_delivery = price_of_delivery;
    }

    public int getAmount_of_order() {
        return amount_of_order;
    }

    public void setAmount_of_order(int amount_of_order) {
        this.amount_of_order = amount_of_order;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}