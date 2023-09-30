package com.Internet.Store.backend.Config;

import com.Internet.Store.backend.Exception.Users.UserIsBlockedException;
import com.Internet.Store.backend.Models.User;
import com.Internet.Store.backend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserService userService;

    @Autowired
    public CustomAuthProvider(UserService userService) {
        this.userService = userService;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userService.findUserByEmail(authentication.getName());
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

        if (!bCrypt.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("Password not match");
        }

        if (user.isBlocked()) {
            throw new UserIsBlockedException();
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getPermission())
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
