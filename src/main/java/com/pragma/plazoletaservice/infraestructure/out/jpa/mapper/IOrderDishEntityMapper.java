package com.pragma.plazoletaservice.infraestructure.out.jpa.mapper;

import com.pragma.plazoletaservice.domain.model.Order;
import com.pragma.plazoletaservice.domain.model.OrderDish;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.OrderDishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderDishEntityMapper {

    @Mapping(target = "dish.id", source = "dish.id")
    OrderDishEntity toEntity(OrderDish orderDish);
    @Mapping(target = "dish.id", source = "dish.id")
    OrderDish toOrderDish(OrderDishEntity orderDishEntity);

    List<OrderDish> toOrderDishList(List<OrderDishEntity> orderDishEntities);

    List<OrderDishEntity> toOrderDishListEntities(List<OrderDish> orderDish);
}
