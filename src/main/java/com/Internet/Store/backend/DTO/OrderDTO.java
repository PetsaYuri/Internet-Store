package com.Internet.Store.backend.DTO;

import com.Internet.Store.backend.Models.Item;

import java.util.List;

public record OrderDTO(Long id, String state, String phone, String address, int price_of_delivery, int amount_of_order, List<Item> items) {
}
