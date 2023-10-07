package com.Internet.Store.backend.Exception.Users;

public class TheBasketIsEmptyException extends RuntimeException {
    public TheBasketIsEmptyException() {
        super("Your basket is empty");
    }
}
