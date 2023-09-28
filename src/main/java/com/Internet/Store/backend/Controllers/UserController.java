package com.Internet.Store.backend.Controllers;

import com.Internet.Store.backend.DTO.UserDTO;
import com.Internet.Store.backend.Models.User;
import com.Internet.Store.backend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public UserDTO getOne(@PathVariable("id") User user) {
        return new UserDTO(user.getName(), user.getEmail(), null, user.getPermission(), user.isBlocked());
    }

    @PostMapping
    public UserDTO create(@RequestBody UserDTO userDTO) {
        User newUser = userService.create(userDTO);
        return new UserDTO(newUser.getName(), newUser.getEmail(), null, newUser.getPermission(), newUser.isBlocked());
    }

    @PutMapping("/{id}")
    public UserDTO update(@RequestBody UserDTO userDTO, @PathVariable("id") User existUser) {
        User updatedUser = userService.update(userDTO, existUser);
        return new UserDTO(updatedUser.getName(), updatedUser.getEmail(), null, updatedUser.getPermission(), updatedUser.isBlocked());
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable("id") User user) {
        return userService.delete(user);
    }
}
