package com.ardent.epicreads.service.impl;

import com.ardent.epicreads.entity.Book;
import com.ardent.epicreads.entity.Cart;
import com.ardent.epicreads.repository.BookRepository;
import com.ardent.epicreads.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final BookRepository bookRepository;

    @Autowired
    public CartServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addToCart(HttpSession session, Long bookId) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        cart.addItem(book);
        session.setAttribute("cart", cart);
    }

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }
        return cart;
    }

    public void updateCart(HttpSession session, Long bookId, int quantity) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.updateItemQuantity(bookId, quantity);
            session.setAttribute("cart", cart);
        }
    }

    public void removeFromCart(HttpSession session, Long bookId) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.removeItem(bookId);
            session.setAttribute("cart", cart);
        }
    }

    @Override
    public void clearCart(HttpSession session, Long userId) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.getItems().clear();
            session.setAttribute("cart", cart);
        }
    }
}
