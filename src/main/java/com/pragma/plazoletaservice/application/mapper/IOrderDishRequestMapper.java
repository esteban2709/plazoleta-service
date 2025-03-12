package com.pragma.plazoletaservice.application.mapper;

import com.pragma.plazoletaservice.application.dto.request.OrderDishRequestDto;
import com.pragma.plazoletaservice.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderDishRequestMapper {

    @Mapping(target = "dish.id", source = "dishId")
    OrderDish toOrderDish(OrderDishRequestDto orderDishRequestDto);
}
