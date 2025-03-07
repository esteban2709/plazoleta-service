package com.pragma.plazoletaservice.application.handler;

import com.pragma.plazoletaservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantClientResponseDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantHandler {
    RestaurantResponseDto saveRestaurant(RestaurantRequestDto restaurantRequestDto);

    List<RestaurantResponseDto> findAllRestaurants();

    List<RestaurantClientResponseDto> findAllRestaurants(Pageable pageable);

    RestaurantResponseDto findRestaurantById(Long id);
}
