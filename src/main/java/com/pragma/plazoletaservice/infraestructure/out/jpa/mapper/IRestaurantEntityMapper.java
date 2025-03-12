package com.pragma.plazoletaservice.infraestructure.out.jpa.mapper;

import com.pragma.plazoletaservice.domain.model.Restaurant;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IRestaurantEntityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "nit", source = "nit")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "urlLogo", source = "urlLogo")
    @Mapping(target = "ownerId", source = "ownerId")
    RestaurantEntity toEntity(Restaurant restaurant);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "nit", source = "nit")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "urlLogo", source = "urlLogo")
    @Mapping(target = "ownerId", source = "ownerId")
    Restaurant toRestaurant(RestaurantEntity restaurantEntity);
    List<Restaurant> toRestaurants(List<RestaurantEntity> restaurantEntities);
}
