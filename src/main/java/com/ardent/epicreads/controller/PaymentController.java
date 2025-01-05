package com.ardent.epicreads.controller;

import com.ardent.epicreads.entity.Cart;
import com.ardent.epicreads.service.PaymentService;
import com.cashfree.ApiResponse;
import com.cashfree.model.OrderEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/checkout")
    public String getPaymentSessionId(@ModelAttribute("cart") Cart cart,
                                      @RequestParam("subTotal") String subTotal,
                                      @RequestParam("discount") String discount,
                                      @RequestParam("shippingCharges") String shippingCharges,
                                      @RequestParam("grandTotal") String grandTotal,
                                      Model model) {

        model.addAttribute("cart", cart);
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("discount", discount);
        model.addAttribute("shippingCharges", shippingCharges);
        model.addAttribute("grandTotal", grandTotal);

        // Will Use After Delivery Address Form Submission (Confirm Address)
//        List<CartItem> items = cart.getItems();
//        for (CartItem item : items) {
//            System.out.println("Book ID: " + item.getBook().getId() + ", Quantity: " + item.getQuantity());
//        }

        return "payment/delivery-address-form";
    }

    @PostMapping("/create-order")
    public String createOrder(@ModelAttribute("cart") Cart cart,
                              @RequestParam String address1,
                              @RequestParam String address2,
                              @RequestParam String city,
                              @RequestParam String state,
                              @RequestParam String zip,
                              HttpSession session,
                              Model model) {

        // Combine the address fields into a single string
        String address = String.join(", ", address1, address2, city, state, zip);

        ApiResponse<OrderEntity> response = paymentService.createCashfreeOrder(cart, session, address);

        System.out.println(response.getData());

        model.addAttribute("paymentDetails", response.getData());
        return "payment/confirm-order";
    }

        @GetMapping("/payment-success")
    public String getPaymentSuccessPage() {
        return "payment/payment-success";
    }

}
