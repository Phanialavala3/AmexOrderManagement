package com.amex.order.service;

import com.amex.order.DTO.OrderDTO;
import com.amex.order.Repository.OrderRepository;
import com.amex.order.Repository.ProductRepository;
import com.amex.order.entity.CustomerOrder;
import com.amex.order.entity.ProductEntity;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final Environment env;

    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper, Environment env,ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        List<ProductEntity> products = new ArrayList<>();
        ProductEntity apple = new ProductEntity();
        ProductEntity orange = new ProductEntity();
        apple.setName("Apple");
        apple.setPrice(Double.valueOf(Objects.requireNonNull(env.getProperty("amex.apple.price"))));
        orange.setName("Orange");
        orange.setPrice(Double.valueOf(Objects.requireNonNull(env.getProperty("amex.orange.price"))));
        products.add(apple);
        products.add(orange);
        productRepository.saveAll(products);
        this.env = env;
    }

    public OrderDTO submitOrder(@org.jetbrains.annotations.NotNull List<String> orderList) {
        Map<String, Integer> orderWithQuantity = new HashMap<>();
        CustomerOrder customerOrder = new CustomerOrder();
        double totalCost = 0.00;

        for (String item : orderList) {
            if (orderWithQuantity.containsKey(item)) {
                orderWithQuantity.put(item, orderWithQuantity.get(item) + 1);
            }
            else{
                orderWithQuantity.put(item,1);
            }
        }
        customerOrder.setOrderList(orderList);
        if (Boolean.parseBoolean(env.getProperty("amex.simpleOffer"))) {
            for (Map.Entry<String, Integer> entry : orderWithQuantity.entrySet()) {
                ProductEntity product;
                product = productRepository.findByName(entry.getKey());
                if (entry.getKey().equals("Apple")) {
                    totalCost += (entry.getValue() / 2) * (product.getPrice() / 2);
                    totalCost = totalCost + ((entry.getValue() % 2) * product.getPrice());
                }
                if (entry.getKey().equals("Orange")) {
                    totalCost += (entry.getValue() / 3) * (product.getPrice() * 2);
                    totalCost = totalCost + ((entry.getValue() % 3) * product.getPrice());
                }
            }
        } else {
            for (Map.Entry<String, Integer> entry : orderWithQuantity.entrySet()) {
                ProductEntity product;
                product = productRepository.findByName(entry.getKey());
                totalCost = totalCost + (entry.getValue() * product.getPrice());
            }
        }
        customerOrder.setTotalOrderCost(totalCost);
        orderRepository.save(customerOrder);
        return modelMapper.map(customerOrder,OrderDTO.class);
    }


}
