package com.pragma.plazoletaservice.infraestructure.out.jpa.mapper;

import com.pragma.plazoletaservice.domain.model.Dish;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishEntityMapper {

    @Mapping(target = "restaurant.id", source = "restaurant.id")
    @Mapping(target = "category.id", source = "category.id")
    DishEntity toDishEntity(Dish dish);

    @Mapping(target = "restaurant.id", source = "restaurant.id")
    @Mapping(target = "category.id", source = "category.id")
    Dish toDish(DishEntity dishEntity);
    List<Dish> toDishList(List<DishEntity> dishEntityList);
}
