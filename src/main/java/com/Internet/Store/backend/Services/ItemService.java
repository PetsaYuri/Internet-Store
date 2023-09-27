package com.Internet.Store.backend.Services;

import com.Internet.Store.backend.DTO.ItemDTO;
import com.Internet.Store.backend.Models.Category;
import com.Internet.Store.backend.Models.Item;
import com.Internet.Store.backend.Repositories.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemsRepository itemsRepository;

    private final CategoryService categoryService;

    @Autowired
    public ItemService(ItemsRepository itemsRepository, CategoryService categoryService) {
        this.itemsRepository = itemsRepository;
        this.categoryService = categoryService;
    }

    public List<Item> getAll() {
        return itemsRepository.findAll();
    }

    public Long generateId() {
        int count = 0;
        while (count < 100) {
            String uuid = String.format("%06d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
            Long id = Long.valueOf(uuid.substring(0, 6));
            Optional<Item> item = itemsRepository.findById(id);
            if (item.isEmpty()) {
                return id;
            }
            count++;
        }
        //here need to create custom exception
        throw new RuntimeException();
    }

    public Item create(ItemDTO itemDTO) {
        Long id = generateId();
        Category category = categoryService.getCategoryById(itemDTO.idCategory());
        Item newItem = new Item(id, itemDTO, category);
        itemsRepository.save(newItem);
        boolean isSuccess = categoryService.addItemToSelectedCategory(newItem, category);
        if (isSuccess) {
            return newItem;
        } else {
            throw new RuntimeException("Item not saved in the list");
        }

    }

    public Item update(ItemDTO itemDTO, Item existItem) {
        if (itemDTO.name() != null) {
            existItem.setName(itemDTO.name());
        }

        if (itemDTO.description() != null) {
            existItem.setDescription(itemDTO.description());
        }

        if (itemDTO.image() != null) {
            existItem.setImage(itemDTO.image());
        }

        if (itemDTO.price() != 0) {
            existItem.setPrice(itemDTO.price());
        }

        if (itemDTO.idCategory() != 0) {
            Category category = categoryService.getCategoryById(itemDTO.idCategory());
            existItem.setCategory(category);
        }

        return itemsRepository.save(existItem);
    }

    public boolean delete(Item item) {
        categoryService.removeItemFromSelectedCategory(item, item.getCategory());
        itemsRepository.delete(item);
        return true;
    }
}
