package com.Internet.Store.backend.Services;

import com.Internet.Store.backend.DTO.UserDTO;
import com.Internet.Store.backend.Exception.Users.EmailAlreadyExistException;
import com.Internet.Store.backend.Models.User;
import com.Internet.Store.backend.Repositories.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public User getUserById(Long id) {
        return usersRepository.getReferenceById(id);
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

    public User update(UserDTO userDTO, Long id) {
        User existUser = usersRepository.getReferenceById(id);
        if (userDTO.name() != null) {
            existUser.setName(userDTO.name());
        }

        if (userDTO.email() != null && !existUser.getEmail().equals(userDTO.email())) {
            if (usersRepository.findByEmail(userDTO.email()) == null) {
                existUser.setEmail(userDTO.email());
            }   else {
                throw new EmailAlreadyExistException();
            }
        }

        if (userDTO.password() != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encryptedPassword = passwordEncoder.encode(userDTO.password());
            existUser.setPassword(encryptedPassword);
        }

        return usersRepository.save(existUser);
    }

    public boolean delete(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }

        User user = usersRepository.getReferenceById(id);
        usersRepository.delete(user);
        return true;
    }
}
