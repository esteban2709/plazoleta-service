package com.pragma.plazoletaservice.application.dto.response;

import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.domain.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponseDto {

    private Long id;
    private List<OrderDishResponseDto> orderDishList;
    private Long clientId;
    private Long chefId;
    private RestaurantResponseDto restaurant;
    private OrderStatus orderStatus;
}
