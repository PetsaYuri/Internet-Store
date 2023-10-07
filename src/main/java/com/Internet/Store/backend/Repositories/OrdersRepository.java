package com.Internet.Store.backend.Repositories;

import com.Internet.Store.backend.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
}
