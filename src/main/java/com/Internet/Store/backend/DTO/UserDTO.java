package com.Internet.Store.backend.DTO;

public record UserDTO(String name, String email, String password, String permission, boolean isBlocked) {
}
