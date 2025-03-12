package com.pragma.plazoletaservice.domain.model;

import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class Order {

    private Long id;
    private List<OrderDish> orderDishList;
    private User chef;
    private User client;
    private String securityCode;
    private Restaurant restaurant;
    private OrderStatus orderStatus;
}
