package com.Internet.Store.backend.Repositories;

import com.Internet.Store.backend.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Page<User> findByPermission(String permission, Pageable pageable);

    Page<User> findByIsBlocked(boolean isBlocked, Pageable pageable);
}
