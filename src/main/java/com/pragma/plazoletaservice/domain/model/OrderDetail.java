package com.pragma.plazoletaservice.domain.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDetail {

    private Long id;
    private Order order;
    private Dish dish;
    private Integer quantity;
    private Double subTotalPrice;
}
