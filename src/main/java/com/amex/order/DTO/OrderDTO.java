package com.amex.order.DTO;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDTO {
    private Long orderID;
    private List<String> orderList;
    private Double totalOrderCost;

}
