package com.Internet.Store.backend.Controllers;

import com.Internet.Store.backend.DTO.CategoryDTO;
import com.Internet.Store.backend.Exception.Categories.CategoryAlreadyExistException;
import com.Internet.Store.backend.Models.Category;
import com.Internet.Store.backend.Services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryDTO getOne(@PathVariable("id") Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return new CategoryDTO(category.getTitle(), category.getDescription(), category.getItems());
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found", ex);
        }
    }

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO categoryDTO) {
        try {
            Category newCategory = categoryService.create(categoryDTO);
            return new CategoryDTO(newCategory.getTitle(), newCategory.getDescription(), newCategory.getItems());
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body is not fully written", ex);
        }   catch (CategoryAlreadyExistException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO) {
        try {
            Category updatedCategory = categoryService.update(id, categoryDTO);
            return new CategoryDTO(updatedCategory.getTitle(), updatedCategory.getDescription(), updatedCategory.getItems());
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body is not fully written");
        }   catch (CategoryAlreadyExistException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        try {
            return categoryService.delete(id);
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }
}
