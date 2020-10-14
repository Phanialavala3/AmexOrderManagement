package com.amex.order.serviceTest;

import com.amex.order.DTO.OrderDTO;
import com.amex.order.Repository.OrderRepository;
import com.amex.order.Repository.ProductRepository;
import com.amex.order.entity.ProductEntity;
import com.amex.order.service.OrderService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    private Environment env;

    @MockBean
    private OrderService service;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    List<String> inputOrder = Arrays.asList("Apple", "Apple", "Orange", "Apple");

    @BeforeEach
    public void setup() {
        service = new OrderService(orderRepository, new ModelMapper(), env , productRepository);
    }

    @BeforeEach
    private List<ProductEntity> buildProductEntity() {
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity apple = new ProductEntity();
        ProductEntity orange = new ProductEntity();
        apple.builder()
                .id((long) 1)
                .name("Apple")
                .price(0.6)
                .build();

        orange.builder()
                .id((long) 1)
                .name("Orange")
                .price(0.25)
                .build();

        productEntities.add(apple);
        productEntities.add(orange);

        return productEntities;
    }

    @Test
   public void submitOrder_WithOut_SimpleOffer_should_save_order_and_calculate_Order_total(){
        //given


        //when
        when(service.submitOrder(inputOrder)).thenReturn(buildOrderDTOWithOutOffer());

        //then
        OrderDTO expectedOrderDTO;
        expectedOrderDTO=buildOrderDTOWithOutOffer();

        OrderDTO resultDTO;
        resultDTO = service.submitOrder(inputOrder);
        assertEquals(expectedOrderDTO.getOrderID(),resultDTO.getOrderID());
        assertEquals(expectedOrderDTO.getOrderList(),resultDTO.getOrderList());
        assertEquals(expectedOrderDTO.getTotalOrderCost(),resultDTO.getTotalOrderCost());

    }

    private OrderDTO buildOrderDTOWithOutOffer(){
        return OrderDTO.builder()
                .orderID((long) 1)
                .orderList(Arrays.asList("Apple","Apple", "Orange", "Apple"))
                .totalOrderCost(2.05)
                .build();
    }


}
