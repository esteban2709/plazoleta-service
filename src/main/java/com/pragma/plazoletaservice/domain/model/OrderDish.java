package com.pragma.plazoletaservice.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
public class OrderDish {

    private Long id;
    private Order order;
    private Dish dish;
    private Integer quantity;
//    private Long subTotalPrice;
}
