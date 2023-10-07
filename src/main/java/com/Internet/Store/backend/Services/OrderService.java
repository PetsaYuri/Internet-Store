package com.Internet.Store.backend.Services;

import com.Internet.Store.backend.DTO.OrderDTO;
import com.Internet.Store.backend.Exception.Users.TheBasketIsEmptyException;
import com.Internet.Store.backend.Models.Item;
import com.Internet.Store.backend.Models.Order;
import com.Internet.Store.backend.Models.User;
import com.Internet.Store.backend.Repositories.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;

    private final UserService userService;

    private final ItemService itemService;

    @Autowired
    public OrderService(OrdersRepository ordersRepository, UserService userService, ItemService itemService) {
        this.ordersRepository = ordersRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    public Long generateId() {
        int count = 0;
        while (count < 100) {
            String uuid = String.format("%06d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
            Long id = Long.valueOf(uuid.substring(0, 6));
            Optional<Order> order = ordersRepository.findById(id);
            if (order.isEmpty()) {
                return id;
            }
            count++;
        }
        throw new RuntimeException();
    }

    public List<Order> getAll() {
        return ordersRepository.findAll();
    }

    public Order getOne(Long id) {
        return ordersRepository.getReferenceById(id);
    }

    public Order create(Item item, OrderDTO orderDTO) {
        User customer = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (itemService.getItemById(item.getId()) != null) {
            Long id = generateId();
            Order newOrder = new Order(id, orderDTO, customer, item);
            ordersRepository.save(newOrder);
            userService.addOrderToCustomer(customer, newOrder);
            itemService.addOrderToItems(newOrder.getItems(), newOrder);
            return newOrder;
        }
        throw new EntityNotFoundException();
    }

    public Order createOrderFromBasket(OrderDTO orderDTO) {
        User customer = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!customer.getBasket().isEmpty()) {
            Long id = generateId();
            int amount_of_order = orderDTO.price_of_delivery();
            for (Item item : customer.getBasket()) {
                amount_of_order += item.getPrice();
            }

            Order newOrder = new Order(id, orderDTO, amount_of_order, customer);
            ordersRepository.save(newOrder);
            userService.addOrderToCustomer(customer, newOrder);
            itemService.addOrderToItems(newOrder.getItems(), newOrder);
            return newOrder;
        }
        throw new TheBasketIsEmptyException();
    }

    public Order edit(Long id, OrderDTO orderDTO) {
        Order existOrder = ordersRepository.getReferenceById(id);
        if (orderDTO.state() != null) {
            existOrder.setState(orderDTO.state());
        }

        if (orderDTO.phone() != null) {
            existOrder.setPhone(orderDTO.phone());
        }

        if (orderDTO.address() != null) {
            existOrder.setAddress(orderDTO.address());
        }

        if (orderDTO.price_of_delivery() != 0) {
            int diff = existOrder.getPrice_of_delivery() - orderDTO.price_of_delivery();
            existOrder.setPrice_of_delivery(orderDTO.price_of_delivery());
            existOrder.setAmount_of_order(existOrder.getAmount_of_order() - diff);
        }

        if (orderDTO.items() != null) {
            int amount_of_order = existOrder.getPrice_of_delivery();
            for (Item item : orderDTO.items()) {
                item = itemService.getItemById(item.getId());
                amount_of_order += item.getPrice();
            }
            itemService.changeOrderInItems(existOrder.getItems(),orderDTO.items(), existOrder);
            existOrder.setItems(orderDTO.items());
            existOrder.setAmount_of_order(amount_of_order);

        }
        return ordersRepository.save(existOrder);
    }

    public boolean delete(Long id) {
        Order order = ordersRepository.getReferenceById(id);
        userService.deleteOrderFromCustomer(order.getCustomer());
        itemService.deleteOrderFromItems(order.getItems());
        ordersRepository.delete(order);
        return true;
    }

    public List<Order> receiveYourOrders() {
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) {
            throw new EntityNotFoundException();
        }
        return user.getOrders();
    }
}