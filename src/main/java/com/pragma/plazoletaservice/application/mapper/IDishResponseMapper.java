package com.pragma.plazoletaservice.application.mapper;

import com.pragma.plazoletaservice.application.dto.response.DishResponseDto;
import com.pragma.plazoletaservice.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishResponseMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "urlImage", source = "urlImage")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    DishResponseDto toResponse(Dish dish);
    List<DishResponseDto> toResponseList(List<Dish> dishList);
}
