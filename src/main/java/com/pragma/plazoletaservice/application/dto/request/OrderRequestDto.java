package com.pragma.plazoletaservice.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    private List<OrderDishRequestDto> orderDishList;
    private Long clientId;
    private Long chefId;
    private Long restaurantId;
}
