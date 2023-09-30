package com.Internet.Store.backend.Exception.Users;

import org.springframework.security.core.AuthenticationException;

public class UserIsBlockedException extends AuthenticationException {
    public UserIsBlockedException() {
        super("The user is blocked");
    }
}
