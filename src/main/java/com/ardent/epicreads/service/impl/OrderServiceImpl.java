package com.ardent.epicreads.service.impl;

import com.ardent.epicreads.entity.*;
import com.ardent.epicreads.repository.BookRepository;
import com.ardent.epicreads.repository.OrderItemRepository;
import com.ardent.epicreads.repository.OrderRepository;
import com.ardent.epicreads.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Order createOrder(String orderId, User user, List<CartItem> cartItems, double totalAmount, String address) {
        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Paid");
        order.setAddress(address);
        order.setTotalAmount(totalAmount);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Book dbBook = bookRepository.findById(cartItem.getBook().getId()).orElseThrow(() -> new RuntimeException("Book not found in database by given ID."));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(dbBook);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(dbBook.getPrice() * cartItem.getQuantity());

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        orderRepository.save(order);

        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }
}
