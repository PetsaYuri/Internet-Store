package com.Internet.Store.backend.Repositories;

import com.Internet.Store.backend.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> {
    public Category findByTitle(String title);
}
