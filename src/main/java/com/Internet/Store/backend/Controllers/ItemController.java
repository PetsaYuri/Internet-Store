package com.Internet.Store.backend.Controllers;

import com.Internet.Store.backend.DTO.ItemDTO;
import com.Internet.Store.backend.Models.Item;
import com.Internet.Store.backend.Services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> getAll() {
        return itemService.getAll();
    }

    @GetMapping("/{id}")
    public ItemDTO getOne(@PathVariable("id") Item item) {
        return new ItemDTO(item.getName(), item.getDescription(), item.getImage(), item.getPrice(), item.getCategory().getId());
    }

    @PostMapping
    public ItemDTO create(@RequestBody ItemDTO itemDTO) {
        Item newItem = itemService.create(itemDTO);
        return new ItemDTO(newItem.getName(), newItem.getDescription(), newItem.getImage(), newItem.getPrice(), newItem.getCategory().getId());
    }

    @PutMapping("/{id}")
    public ItemDTO update(@RequestBody ItemDTO itemDTO, @PathVariable("id") Item existItem) {
        Item updatedItem = itemService.update(itemDTO, existItem);
        return new ItemDTO(updatedItem.getName(), updatedItem.getDescription(), updatedItem.getImage(), updatedItem.getPrice(), updatedItem.getCategory().getId());
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Item item) {
        return itemService.delete(item);
    }
}
