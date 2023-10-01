package com.Internet.Store.backend.Controllers;

import com.Internet.Store.backend.DTO.UserDTO;
import com.Internet.Store.backend.Exception.Users.EmailAlreadyExistException;
import com.Internet.Store.backend.Models.User;
import com.Internet.Store.backend.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public List<UserDTO> getAll() {
        List<User> users = userService.getAll();
        return users.stream().map(user -> new UserDTO(user.getName(), user.getEmail(), null, user.getPermission(), user.isBlocked())).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getOne(@PathVariable("id") Long id) {
        try {
            User user = userService.getUserById(id);
            return new UserDTO(user.getName(), user.getEmail(), null, user.getPermission(), user.isBlocked());
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping
    public UserDTO create(@RequestBody UserDTO userDTO) {
        try {
            User newUser = userService.create(userDTO);
            return new UserDTO(newUser.getName(), newUser.getEmail(), null, newUser.getPermission(), newUser.isBlocked());
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
            return new UserDTO(updatedUser.getName(), updatedUser.getEmail(), null, updatedUser.getPermission(), updatedUser.isBlocked());
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
