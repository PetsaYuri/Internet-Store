package com.Internet.Store.backend.Exception.Categories;

public class CategoryAlreadyExistException extends RuntimeException{
    public CategoryAlreadyExistException(String message) {
        super(message);
    }
}
