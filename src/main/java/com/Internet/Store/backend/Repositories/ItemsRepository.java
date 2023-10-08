package com.Internet.Store.backend.Repositories;

import com.Internet.Store.backend.Models.Category;
import com.Internet.Store.backend.Models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Item, Long> {

    Page<Item> findByNameIsContaining(String title, Pageable pageable);

    Page<Item> findByCategory(Category category, Pageable pageable);

    Page<Item> findByPrice(int price, Pageable pageable);
}