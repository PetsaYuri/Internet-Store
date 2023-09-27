package com.Internet.Store.backend.Controllers;

import com.Internet.Store.backend.DTO.CategoryDTO;
import com.Internet.Store.backend.Models.Category;
import com.Internet.Store.backend.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CategoryDTO getOne(@PathVariable("id") Category category) {
        return new CategoryDTO(category.getTitle(), category.getDescription(), category.getItems());
    }

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO categoryDTO) {
        Category newCategory = categoryService.create(categoryDTO);
        return new CategoryDTO(newCategory.getTitle(), newCategory.getDescription(), newCategory.getItems());
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable("id") Category existCategory, @RequestBody CategoryDTO categoryDTO) {
        Category updatedCategory = categoryService.update(existCategory, categoryDTO);
        return new CategoryDTO(updatedCategory.getTitle(), updatedCategory.getDescription(), updatedCategory.getItems());
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Category category) {
        return categoryService.delete(category);
    }
}
