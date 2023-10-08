package com.Internet.Store.backend.Repositories;

import com.Internet.Store.backend.Models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {

    Page<Order> findByState(String state, Pageable pageable);

    Page<Order> findByPhone(String phone, Pageable pageable);
}