package com.pragma.plazoletaservice.application.mapper;

import com.pragma.plazoletaservice.application.dto.request.OrderRequestDto;
import com.pragma.plazoletaservice.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper {

    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "chef.id", source = "chefId")
    @Mapping(target = "client.id", source = "clientId")
    Order toOrder(OrderRequestDto orderRequestDto);

}
