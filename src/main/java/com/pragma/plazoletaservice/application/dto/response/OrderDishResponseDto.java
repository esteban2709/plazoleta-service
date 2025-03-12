package com.pragma.plazoletaservice.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDishResponseDto {

    private Long id;
    private DishResponseDto dish;
    private Integer quantity;
}
