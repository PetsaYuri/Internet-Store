package com.Internet.Store.backend.Controllers;

import com.Internet.Store.backend.DTO.ItemDTO;
import com.Internet.Store.backend.DTO.OrderDTO;
import com.Internet.Store.backend.Exception.Items.ItemIsAlreadyInBasketException;
import com.Internet.Store.backend.Models.Item;
import com.Internet.Store.backend.Models.Order;
import com.Internet.Store.backend.Services.ItemService;
import com.Internet.Store.backend.Services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    private final OrderService orderService;

    @Autowired
    public ItemController(ItemService itemService, OrderService orderService) {
        this.itemService = itemService;
        this.orderService = orderService;
    }

    @GetMapping
    public List<Item> getAll() {
        return itemService.getAll();
    }

    @GetMapping("/{id}")
    public ItemDTO getOne(@PathVariable("id") Long id) {
        try {
            Item item = itemService.getItemById(id);
            return new ItemDTO(item.getName(), item.getDescription(), item.getImage(), item.getPrice(), item.getCategory().getId());
        }   catch (EntityNotFoundException ex) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found", ex);
        }
    }

    @PostMapping
    public ItemDTO create(@RequestBody ItemDTO itemDTO) {
        try {
            Item newItem = itemService.create(itemDTO);
            return new ItemDTO(newItem.getName(), newItem.getDescription(), newItem.getImage(), newItem.getPrice(), newItem.getCategory().getId());
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body is not fully written", ex);
        }
    }

    @PostMapping("/{id}/addToBasket")
    public boolean addToBasket(@PathVariable("id") Long id) {
        try {
            return itemService.setUserToWillingList(id);
        }   catch (ItemIsAlreadyInBasketException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/{id}/buy")
    public OrderDTO buy(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO) {
        try {
            Item item = itemService.getItemById(id);
            Order order = orderService.create(item, orderDTO);
            return new OrderDTO(order.getId(), order.getState(), order.getPhone(), order.getAddress(), order.getPrice_of_delivery(), order.getAmount_of_order(), order.getItems());
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The item not found");
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body is not fully written");
        }
    }

    @PutMapping("/{id}")
    public ItemDTO update(@RequestBody ItemDTO itemDTO, @PathVariable("id") Long id) {
        try {
            Item updatedItem = itemService.update(itemDTO, id);
            return new ItemDTO(updatedItem.getName(), updatedItem.getDescription(), updatedItem.getImage(), updatedItem.getPrice(), updatedItem.getCategory().getId());
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found", ex);
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException((HttpStatus.BAD_REQUEST), "The body is not fully written", ex);
        }
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        try {
            return itemService.delete(id);
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found", ex);
        }
    }
}
