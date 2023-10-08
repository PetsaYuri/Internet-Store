package com.Internet.Store.backend.Controllers;

import com.Internet.Store.backend.DTO.OrderDTO;
import com.Internet.Store.backend.Models.Order;
import com.Internet.Store.backend.Services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAll(@RequestParam(name = "state", required = false) String state, @RequestParam(name = "phone", required = false) String phone,
                              @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                              @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        try {
            if (state != null) {
                return orderService.getOrdersByState(state, PageRequest.of(page, size));
            }   else if(phone != null) {
                return orderService.getOrdersByPhone(phone, PageRequest.of(page, size));
            }   else {
                return orderService.getAll(PageRequest.of(page, size));
            }
        }   catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Order getOne(@PathVariable("id") Long id) {
        try {
            return orderService.getOne(id);
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found");
        }
    }

    @GetMapping("/myOrders")
    public List<OrderDTO> receiveYourOrders() {
        try {
            return orderService.receiveYourOrders().stream().map(order -> new OrderDTO(order.getId(), order.getState(), order.getPhone(), order.getAddress(), order.getPrice_of_delivery(),
                    order.getAmount_of_order(), order.getItems())).toList();
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order list not found");
        }
    }

    @PostMapping
    public Order create(@RequestBody OrderDTO orderDTO) {
        try {
            return orderService.createOrderFromBasket(orderDTO);
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body is not fully written");
        }
    }

    @PutMapping("/{id}")
    public Order edit(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO) {
        try {
            return orderService.edit(id, orderDTO);
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }   catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body if not fully written");
        }
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        try {
            return orderService.delete(id);
        }   catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }
}