package com.ardent.epicreads.service;

import com.ardent.epicreads.entity.CartItem;
import com.ardent.epicreads.entity.Order;
import com.ardent.epicreads.entity.User;

import java.util.List;

public interface OrderService {
    Order createOrder(String orderId, User user, List<CartItem> cartItems, double totalAmount, String address);
    List<Order> getAllOrders();
    List<Order> getOrdersByUser(User user);
    Order getOrderById(String id);
}
