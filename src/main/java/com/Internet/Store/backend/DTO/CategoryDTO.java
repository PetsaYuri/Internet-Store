package com.Internet.Store.backend.DTO;

import com.Internet.Store.backend.Models.Item;

import java.util.List;

public record CategoryDTO(String title, String description, List<Item> items) {
}
