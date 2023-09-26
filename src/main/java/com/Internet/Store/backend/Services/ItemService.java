package com.Internet.Store.backend.Services;

import com.Internet.Store.backend.DTO.ItemDTO;
import com.Internet.Store.backend.Models.Item;
import com.Internet.Store.backend.Repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    public Long generateId() {
        int count = 0;
        while (count < 100) {
            String uuid = String.format("%06d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
            Long id = Long.valueOf(uuid.substring(0, 6));
            Optional<Item> item = itemRepository.findById(id);
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
        Item newItem = new Item(id, itemDTO.name(), itemDTO.description(), itemDTO.image(), itemDTO.price());
        return itemRepository.save(newItem);
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

        return itemRepository.save(existItem);
    }

    public boolean delete(Item item) {
        itemRepository.delete(item);
        return true;
    }
}
