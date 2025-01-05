package com.ardent.epicreads.service;

import com.ardent.epicreads.entity.Cart;
import jakarta.servlet.http.HttpSession;

public interface CartService {
    void addToCart(HttpSession session, Long bookId);
    Cart getCart(HttpSession session);
    void updateCart(HttpSession session, Long bookId, int quantity);
    void removeFromCart(HttpSession session, Long bookId);
    void clearCart(HttpSession session, Long userId);
}
