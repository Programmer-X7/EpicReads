package com.ardent.epicreads.controller;

import com.ardent.epicreads.entity.Order;
import com.ardent.epicreads.entity.User;
import com.ardent.epicreads.service.CartService;
import com.ardent.epicreads.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    //    Note: Order will be created by backend during checkout
    //    -----------------------------------------------------

    // Get Orders for user
    @GetMapping("/orders")
    public String getOrderByUser(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        List<Order> ordersByUser = orderService.getOrdersByUser(user);

        // Format dates before adding to the model
//        ordersByUser.forEach(order -> order.setFormattedOrderDate(
//                new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderDate())
//        ));

        model.addAttribute("orders", ordersByUser);

        return "orders";
    }

    // Get All Orders (Admin)
    @GetMapping("/admin/orders")
    public String getAllOrders(Model model, HttpServletRequest request) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/admin-orders";
    }

    // Get Order Details (User)
    @GetMapping("/orders/user/{orderId}")
    public String getUserOrderDetails(@PathVariable String orderId, Model model) {
        Order orderDetails = orderService.getOrderById(orderId);
        model.addAttribute("orderDetails", orderDetails);
        return "order-details";
    }

    // Get Order Details (
    @GetMapping("/admin/orders/{orderId}")
    public String getAdminOrderDetails(@PathVariable String orderId, Model model, HttpServletRequest request) {
        Order orderDetails = orderService.getOrderById(orderId);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/admin-order-details";
    }
}
