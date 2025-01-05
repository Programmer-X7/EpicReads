package com.ardent.epicreads.service;

import com.ardent.epicreads.entity.Cart;
import com.cashfree.ApiResponse;
import com.cashfree.model.OrderEntity;
import jakarta.servlet.http.HttpSession;

public interface PaymentService {
    ApiResponse<OrderEntity> createCashfreeOrder(Cart cart, HttpSession session, String address);
}
