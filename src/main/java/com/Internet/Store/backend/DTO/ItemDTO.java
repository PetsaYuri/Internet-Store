package com.Internet.Store.backend.DTO;

import com.Internet.Store.backend.Models.Category;

public record ItemDTO(String name, String description, String image, int price, Long idCategory) {
}
