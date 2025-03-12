package com.pragma.plazoletaservice.infraestructure.out.jpa.mapper;

import com.pragma.plazoletaservice.domain.model.Order;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IRestaurantEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderEntityMapper {

    @Mapping(target = "restaurant", source = "restaurant")
    @Mapping(target = "chefId", source = "chef.id")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "securityCode", source = "securityCode")
    OrderEntity toEntity(Order order);
    @Mapping(target = "restaurant", source = "restaurant")
    @Mapping(target = "chef.id", source = "chefId")
    @Mapping(target = "client.id", source = "clientId")
    @Mapping(target = "securityCode", source = "securityCode")
    Order toOrder(OrderEntity orderEntity);

    List<Order> toOrderList(List<OrderEntity> orderEntities);
}
