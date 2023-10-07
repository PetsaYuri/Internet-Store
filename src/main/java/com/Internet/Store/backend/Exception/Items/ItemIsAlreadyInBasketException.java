package com.Internet.Store.backend.Exception.Items;

public class ItemIsAlreadyInBasketException extends RuntimeException {
    public ItemIsAlreadyInBasketException() {
        super("The item is already in your shopping basket");
    }
}
