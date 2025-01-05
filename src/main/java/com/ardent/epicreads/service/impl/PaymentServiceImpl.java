package com.ardent.epicreads.service.impl;

import com.ardent.epicreads.entity.Book;
import com.ardent.epicreads.entity.Cart;
import com.ardent.epicreads.entity.CartItem;
import com.ardent.epicreads.entity.User;
import com.ardent.epicreads.repository.BookRepository;
import com.ardent.epicreads.service.CartService;
import com.ardent.epicreads.service.OrderService;
import com.ardent.epicreads.service.PaymentService;
import com.ardent.epicreads.uitls.CashfreeUtil;
import com.cashfree.ApiResponse;
import com.cashfree.model.OrderEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final CashfreeUtil cashfreeUtil;
    private final BookRepository bookRepository;
    private final OrderService orderService;
    private final CartService cartService;

    @Autowired
    public PaymentServiceImpl(CashfreeUtil cashfreeUtil, BookRepository bookRepository, OrderService orderService, CartService cartService) {
        this.cashfreeUtil = cashfreeUtil;
        this.bookRepository = bookRepository;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @Override
    public ApiResponse<OrderEntity> createCashfreeOrder(Cart cart, HttpSession session, String address) {

//        List<CartItem> items = cart.getItems();
//        for (CartItem item : items) {
//            System.out.println("Book ID: " + item.getBook().getId() + ", Quantity: " + item.getQuantity());
//        }

        // ----------------- Create Order ID (For Database)  -----------------
        long currentTimeMillis = System.currentTimeMillis();
        int randomNumber = new Random().nextInt(900000) + 100000;
        String orderId = "order_" + currentTimeMillis + randomNumber;

        // Calculate Order Amount
        double orderAmount = 0;

        List<CartItem> items = cart.getItems();
        for (CartItem item : items) {
            Book dbBook = bookRepository.findById(item.getBook().getId()).orElse(null);

            double subTotalPrice = dbBook.getPrice() * item.getQuantity();
            orderAmount += subTotalPrice;
        }

        double discount = orderAmount * 0.1; // 10% discount
        double shippingCharges = (orderAmount > 0 && orderAmount < 800) ? 40 : 0;
        orderAmount = orderAmount - discount + shippingCharges;


        // ---------------  Get User  -----------------
        User user = (User) session.getAttribute("user");

        // Create Order For DB
        orderService.createOrder(orderId, user, items, orderAmount, address);

        // Clear the cart
        cartService.clearCart(session, user.getId());

        return cashfreeUtil.getCashfreeOrderResponse(orderId, orderAmount, user);
    }
}
