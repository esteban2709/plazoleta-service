package com.pragma.plazoletaservice.application.mapper;

import com.pragma.plazoletaservice.application.dto.response.OrderResponseDto;
import com.pragma.plazoletaservice.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IRestaurantResponseMapper.class, IOrderDishResponseMapper.class},
unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {

    @Mapping(target = "restaurant", source = "restaurant")
    @Mapping(target = "chefId", source = "chef.id")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "orderDishList", source = "orderDishList")
    OrderResponseDto toResponse(Order order);

    List<OrderResponseDto> toResponseList(List<Order> orders);

}
