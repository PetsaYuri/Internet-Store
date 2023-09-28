package com.Internet.Store.backend.Exception.Users;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        super("The user with this email already exists in our store");
    }
}
