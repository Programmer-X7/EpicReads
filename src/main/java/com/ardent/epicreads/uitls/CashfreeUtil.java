package com.ardent.epicreads.uitls;

import com.ardent.epicreads.entity.User;
import com.cashfree.ApiException;
import com.cashfree.ApiResponse;
import com.cashfree.*;
import com.cashfree.model.CreateOrderRequest;
import com.cashfree.model.CustomerDetails;
import com.cashfree.model.OrderEntity;
import com.cashfree.model.OrderMeta;
import org.springframework.stereotype.Component;

@Component
public class CashfreeUtil {

    Cashfree cashfree = new Cashfree();
    String xApiVersion = "2022-09-01";
    String returnUrl = "https://epicread-bookstore.onrender.com/payment/payment-success";


    public ApiResponse<OrderEntity> getCashfreeOrderResponse (String orderId,
                                                              double orderAmount,
                                                              User user) {

        // Order Details
        CreateOrderRequest request = new CreateOrderRequest();
        request.setOrderId(orderId);
        request.setOrderAmount(orderAmount);
        request.setOrderCurrency("INR");

        // Customer Details
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerId(user.getId().toString());
        customerDetails.setCustomerName((user.getFirstName() + " " + user.getLastName()));
        customerDetails.setCustomerPhone(user.getPhoneNumber());
        customerDetails.setCustomerEmail(user.getUserName());
        request.setCustomerDetails(customerDetails);

        // Order Meta Data
        OrderMeta orderMeta = new OrderMeta();
        orderMeta.setReturnUrl(returnUrl);
        request.setOrderMeta(orderMeta);

        ApiResponse<OrderEntity> response = null;

        try {
            response = cashfree.PGCreateOrder(xApiVersion, request, null, null, null);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
