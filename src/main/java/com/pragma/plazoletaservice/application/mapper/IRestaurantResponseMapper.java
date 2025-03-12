package com.pragma.plazoletaservice.application.mapper;

import com.pragma.plazoletaservice.application.dto.response.RestaurantClientResponseDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazoletaservice.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "nit", source = "nit")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "urlLogo", source = "urlLogo")
    @Mapping(target = "ownerId", source = "ownerId")
    RestaurantResponseDto toResponse(Restaurant restaurant);

    List<RestaurantClientResponseDto> toResponseClientList(List<Restaurant> restaurants);

    List<RestaurantResponseDto> toResponseList(List<Restaurant> restaurants);
}
