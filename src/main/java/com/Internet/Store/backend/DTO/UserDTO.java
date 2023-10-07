package com.Internet.Store.backend.DTO;

import com.Internet.Store.backend.Models.Item;
import com.Internet.Store.backend.Models.Order;

import java.util.List;

public record UserDTO(String name, String email, String password, String permission, boolean isBlocked, List<Item> basket, List<Order> orders) {
}
