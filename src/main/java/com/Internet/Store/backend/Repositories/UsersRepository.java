package com.Internet.Store.backend.Repositories;

import com.Internet.Store.backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);
}
