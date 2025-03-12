package com.pragma.plazoletaservice.application.mapper;

import com.pragma.plazoletaservice.application.dto.response.OrderDishResponseDto;
import com.pragma.plazoletaservice.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {IDishResponseMapper.class}, // Add dish mapper here
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderDishResponseMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dish", source = "dish")
    @Mapping(target = "quantity", source = "quantity")
    OrderDishResponseDto toResponse(OrderDish orderDish);

    List<OrderDishResponseDto> toResponseList(List<OrderDish> orderDishes);
}