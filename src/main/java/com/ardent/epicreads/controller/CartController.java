package com.ardent.epicreads.controller;

import com.ardent.epicreads.entity.Cart;
import com.ardent.epicreads.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, HttpSession session, HttpServletRequest request) {
        cartService.addToCart(session, bookId);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/buyNow/{bookId}")
    public String buyNow(@PathVariable Long bookId, HttpSession session) {
        cartService.addToCart(session, bookId);
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        Cart cart = cartService.getCart(session);

        double subtotalFloat = cart.getItems().stream().mapToDouble(item -> item.getQuantity() * item.getBook().getPrice()).sum();
        double discountFloat = subtotalFloat * 0.1; // 10% discount
        double shippingChargesFloat = (subtotalFloat > 0 && subtotalFloat < 800) ? 40 : 0;
        double grandTotalFloat = subtotalFloat - discountFloat + shippingChargesFloat;

        // Convert double values to integers
        int subtotal = (int) subtotalFloat;
        int discount = (int) discountFloat;
        int shippingCharges = (int) shippingChargesFloat;
        int grandTotal = (int) grandTotalFloat;


        model.addAttribute("cart", cart);
        model.addAttribute("totalItems", cart.getTotalItems());
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("discount", discount);
        model.addAttribute("shippingCharges", shippingCharges);
        model.addAttribute("grandTotal", grandTotal);

        return "cart";
    }

    @PostMapping("/update/{bookId}")
    public String updateCart(@PathVariable Long bookId, @RequestParam(value = "quantity", defaultValue = "1") String quantityStr,
                             @RequestParam("action") String action, HttpSession session) {

        System.out.println("Received action: " + action);
        System.out.println("Received quantity: " + quantityStr);

        int quantity;

        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            quantity = 1; // Default to 1 if there's an error
        }

        if (action.equals("plus")) {
            quantity++;
        } else if (action.equals("minus") && quantity > 1) {
            quantity--;
        }

        System.out.println("Updated quantity: " + quantity);

        cartService.updateCart(session, bookId, quantity);
        return "redirect:/cart";
    }


    @PostMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable Long bookId, HttpSession session) {
        cartService.removeFromCart(session, bookId);
        return "redirect:/cart";
    }
}