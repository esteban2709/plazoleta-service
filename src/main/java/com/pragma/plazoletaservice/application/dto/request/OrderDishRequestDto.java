package com.pragma.plazoletaservice.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDishRequestDto {

    private Long dishId;
    private Long quantity;
}
