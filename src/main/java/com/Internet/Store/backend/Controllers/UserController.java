package com.Internet.Store.backend.Controllers;

import com.Internet.Store.backend.DTO.UserDTO;
import com.Internet.Store.backend.Exception.Users.EmailAlreadyExistException;
import com.Internet.Store.backend.Models.User;
import com.Internet.Store.backend.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAll(@RequestParam(name = "permission", required = false) String permission, @RequestParam(name = "isBlocked", required = false) String isBlocked,
                                @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        try {
            List<User> users;
            if (permission != null) {
                users = userService.getUsersByPermission(permission, PageRequest.of(page, size));
            }   else if(isBlocked != null) {
                users = userService.getUsersByBlocked(Boolean.parseBoolean(isBlocked), PageRequest.of(page, size));
            }   else {
                users = userService.getAll(PageRequest.of(page, size));
            }

            return users.stream().map(user ->
                    new UserDTO(user.getName(), user.getEmail(), null, user.getPermission(), user.isBlocked(), user.getBasket(), user.getOrders())).collect(Collectors.toList());
        }   catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public UserDTO getOne(@PathVariable("id") Long id) {
        try {
            User user = userService.getUserById(id);
            return new UserDTO(user.getName(), user.getEmail(), null, user.getPermission(), user.isBlocked(), user.getBasket(), user.getOrders());
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/profile")
    public UserDTO viewYourProfile() {
        try {
            User user = userService.getUserDetails();
            return new UserDTO(user.getName(), user.getEmail(), null, user.getPermission(), user.isBlocked(), user.getBasket(), user.getOrders());
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You must be authorised");
        }
    }

    @PostMapping("/signup")
    public UserDTO signUp(@RequestBody UserDTO userDTO) {
        try {
            User createdUser = userService.create(userDTO);
            return new UserDTO(createdUser.getName(), createdUser.getEmail(), null, createdUser.getPermission(), createdUser.isBlocked(), createdUser.getBasket(),
                    createdUser.getOrders());
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body is not fully written");
        }   catch (EmailAlreadyExistException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping
    public UserDTO create(@RequestBody UserDTO userDTO) {
        try {
            User newUser = userService.create(userDTO);
            return new UserDTO(newUser.getName(), newUser.getEmail(), null, newUser.getPermission(), newUser.isBlocked(), newUser.getBasket(), newUser.getOrders());
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body is not fully written");
        }   catch (EmailAlreadyExistException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public UserDTO update(@RequestBody UserDTO userDTO, @PathVariable("id") Long id) {
        try {
            User updatedUser = userService.update(userDTO, id);
            return new UserDTO(updatedUser.getName(), updatedUser.getEmail(), null, updatedUser.getPermission(), updatedUser.isBlocked(), updatedUser.getBasket(),
                    updatedUser.getOrders());
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body is not fully written");
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }   catch (EmailAlreadyExistException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable("id") Long id) {
        try {
            return userService.delete(id);
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}