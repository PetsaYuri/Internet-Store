package com.Internet.Store.backend.Services;

import com.Internet.Store.backend.DTO.ItemDTO;
import com.Internet.Store.backend.Models.Category;
import com.Internet.Store.backend.Models.Item;
import com.Internet.Store.backend.Models.Order;
import com.Internet.Store.backend.Models.User;
import com.Internet.Store.backend.Repositories.ItemsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemsRepository itemsRepository;

    private final CategoryService categoryService;

    private final UserService userService;

    @Autowired
    public ItemService(ItemsRepository itemsRepository, CategoryService categoryService, UserService userService) {
        this.itemsRepository = itemsRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    public List<Item> getAll() {
        return itemsRepository.findAll();
    }

    public Item getItemById(Long id) throws EntityNotFoundException {
        return itemsRepository.getReferenceById(id);
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

    public Item update(ItemDTO itemDTO, Long id) {
        Item existItem = itemsRepository.getReferenceById(id);
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

        if (itemDTO.idCategory() != null) {
            Category category = categoryService.getCategoryById(itemDTO.idCategory());
            existItem.setCategory(category);
        }

        return itemsRepository.save(existItem);
    }

    public boolean delete(Long id) {
        Item item = itemsRepository.getReferenceById(id);
        categoryService.removeItemFromSelectedCategory(item, item.getCategory());
        itemsRepository.delete(item);
        return true;
    }

    public boolean setUserToWillingList(Long id) {
        Item item = itemsRepository.getReferenceById(id);
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) {
            throw new EntityNotFoundException();
        }

        userService.addItemToBasket(user, item);
        item.getListOfWillingUsers().add(user);
        itemsRepository.save(item);
        return true;
    }

    public void addOrderToItems(List<Item> items, Order order) {
        itemsRepository.saveAll(items.stream().peek(item -> item.getOrders().add(order)).toList());
    }

    public void changeOrderInItems(List<Item> oldItemsList, List<Item> newitemList, Order order) {
        itemsRepository.saveAll(oldItemsList.stream().peek(item -> item.getOrders().remove(order)).toList());
        itemsRepository.saveAll(newitemList.stream().peek(item -> {
            try {
                item.getOrders().add(order);
            }   catch (NullPointerException ex) {
                item.setOrders(new ArrayList<>());
                item.getOrders().add(order);
            }
        }).toList());
    }

    public void deleteOrderFromItems(List<Item> items) {
        List<Item> updatedItems = items.stream().peek(item -> item.setOrders(new ArrayList<>())).toList();
        itemsRepository.saveAll(updatedItems);
    }
}