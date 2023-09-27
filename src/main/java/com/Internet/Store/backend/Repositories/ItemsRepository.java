package com.Internet.Store.backend.Repositories;

import com.Internet.Store.backend.Models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<Item, Long> {
}
