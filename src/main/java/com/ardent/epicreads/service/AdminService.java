package com.ardent.epicreads.service;

import com.ardent.epicreads.entity.Order;

import java.util.List;

public interface AdminService {
    long getTotalCustomers();
    long getOutOfStockProductCount();
    double getMonthlyRevenue();
    long getTotalProducts();
    long getMonthlyOrdersCount();
    double getTotalRevenue();
    List<Order> getLast5Orders();
}
