package com.ardent.epicreads.service.impl;

import com.ardent.epicreads.entity.Order;
import com.ardent.epicreads.repository.BookRepository;
import com.ardent.epicreads.repository.OrderRepository;
import com.ardent.epicreads.repository.UserRepository;
import com.ardent.epicreads.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, BookRepository bookRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public long getTotalCustomers() {
        return userRepository.countCustomers();
    }

    @Override
    public long getOutOfStockProductCount() {
        return bookRepository.countOutOfStockProducts();
    }

    @Override
    public double getMonthlyRevenue() {
        double monthlyRevenue = orderRepository.findMonthlyRevenue();

        // Create a DecimalFormat instance with the desired pattern
        DecimalFormat df = new DecimalFormat("#.00");

        // Format the monthlyRevenue value to 2 decimal places
        String formattedRevenue = df.format(monthlyRevenue);

        // Parse the formatted string back to double
        return Double.parseDouble(formattedRevenue);
    }

    @Override
    public long getTotalProducts() {
        return bookRepository.count();
    }

    @Override
    public long getMonthlyOrdersCount() {
        return orderRepository.countMonthlyOrders();
    }

    @Override
    public double getTotalRevenue() {
        double totalRevenue = orderRepository.findTotalRevenue();

        // Create a DecimalFormat instance with the desired pattern
        DecimalFormat df = new DecimalFormat("#.00");

        // Format the monthlyRevenue value to 2 decimal places
        String formattedRevenue = df.format(totalRevenue);

        // Parse the formatted string back to double
        return Double.parseDouble(formattedRevenue);
    }

    @Override
    public List<Order> getLast5Orders() {
        return orderRepository.findTop5OrdersByOrderDateDesc();
    }
}
