package com.Internet.Store.backend.Services;

import com.Internet.Store.backend.DTO.UserDTO;
import com.Internet.Store.backend.Exception.Users.EmailAlreadyExistException;
import com.Internet.Store.backend.Models.User;
import com.Internet.Store.backend.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> getAll() {
        return usersRepository.findAll();
    }

    public User create(UserDTO userDTO) {
        User existUser = usersRepository.findByEmail(userDTO.email());
        if (existUser == null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encryptedPassword = passwordEncoder.encode(userDTO.password());
            User newUser = new User(encryptedPassword, userDTO);
            return usersRepository.save(newUser);
        }   else {
            throw new EmailAlreadyExistException();
        }
    }

    public User update(UserDTO userDTO, User existUser) {
        if (userDTO.name() != null) {
            existUser.setName(userDTO.name());
        }

        if (userDTO.email() != null && !existUser.getEmail().equals(userDTO.email())) {
            existUser.setEmail(userDTO.email());
        }

        if (userDTO.password() != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encryptedPassword = passwordEncoder.encode(userDTO.password());
            existUser.setPassword(encryptedPassword);
        }

        return usersRepository.save(existUser);
    }

    public boolean delete(User user) {
        usersRepository.delete(user);
        return true;
    }
}
