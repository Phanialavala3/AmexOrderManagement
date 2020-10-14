package com.amex.order.controller;

import com.amex.order.DTO.OrderDTO;
import com.amex.order.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AmexOrderController {
    private final OrderService orderService;

    @Value("#{'${amex.order}'.split(',')}")
    public List<String> products;

    public AmexOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/amex/placeOrder")
    public OrderDTO submitOrder() {
        return orderService.submitOrder(products);
    }


}
