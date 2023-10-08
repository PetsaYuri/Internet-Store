package com.Internet.Store.backend.Services;

import com.Internet.Store.backend.DTO.CategoryDTO;
import com.Internet.Store.backend.Exception.Categories.CategoryAlreadyExistException;
import com.Internet.Store.backend.Models.Category;
import com.Internet.Store.backend.Models.Item;
import com.Internet.Store.backend.Repositories.CategoriesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoryService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Category> getAll() {
        return categoriesRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoriesRepository.getReferenceById(id);
    }

    public Category getCategoryByTitle(String title) {
        return categoriesRepository.findByTitle(title);
    }

    public Category create(CategoryDTO categoryDTO) {
        Category existCategory = categoriesRepository.findByTitle(categoryDTO.title());
        if (existCategory != null) {
            throw new CategoryAlreadyExistException("The category with this title already exists");
        }
        Category newCategory = new Category(categoryDTO);
        return categoriesRepository.save(newCategory);
    }

    public Category update(Long id, CategoryDTO categoryDTO) {
        Category existCategory = categoriesRepository.getReferenceById(id);
        if (categoryDTO.title() != null) {
            Category category = categoriesRepository.findByTitle(categoryDTO.title());
            if (category != null) {
                throw new CategoryAlreadyExistException("The category with this title already exists");
            }
            existCategory.setTitle(categoryDTO.title());
        }

        if (categoryDTO.description() != null) {
            existCategory.setDescription(categoryDTO.description());
        }

        return categoriesRepository.save(existCategory);
    }

    public boolean delete(Long id) {
        if (!categoriesRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        Category category = categoriesRepository.getReferenceById(id);
        categoriesRepository.delete(category);
        return true;
    }

    public boolean addItemToSelectedCategory(Item item, Category category) {
        List<Item> newItemList = category.getItems();
        newItemList.add(item);
        category.setItems(newItemList);
        categoriesRepository.save(category);
        return true;
    }

    public void removeItemFromSelectedCategory(Item item, Category category) {
        List<Item> newItemList = category.getItems();
        boolean isRemoved = newItemList.remove(item);
        if (isRemoved) {
            category.setItems(newItemList);
            categoriesRepository.save(category);
        }   else {
            throw new EntityNotFoundException();
        }
    }
}