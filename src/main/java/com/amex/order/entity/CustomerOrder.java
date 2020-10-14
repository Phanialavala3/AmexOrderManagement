package com.amex.order.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;
    @ElementCollection
    private List<String> orderList;
    private Double totalOrderCost;

}
