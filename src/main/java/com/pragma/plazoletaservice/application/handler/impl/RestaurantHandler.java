package com.pragma.plazoletaservice.application.handler.impl;

import com.pragma.plazoletaservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazoletaservice.application.handler.IRestaurantHandler;
import com.pragma.plazoletaservice.application.mapper.IRestaurantRequestMapper;
import com.pragma.plazoletaservice.application.mapper.IRestaurantResponseMapper;
import com.pragma.plazoletaservice.domain.api.IRestaurantServicePort;
import com.pragma.plazoletaservice.domain.model.Restaurant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantResponseMapper restaurantResponseMapper;
    private final IRestaurantRequestMapper restaurantRequestMapper;

    @Override
    public RestaurantResponseDto saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        return restaurantResponseMapper.toResponse(restaurantServicePort.saveRestaurant(restaurant));
    }

    @Override
    public List<RestaurantResponseDto> findAllRestaurants() {
        return restaurantResponseMapper.toResponseList(restaurantServicePort.findAllRestaurants());
    }

    @Override
    public RestaurantResponseDto findRestaurantById(Long id) {
        return restaurantResponseMapper.toResponse(restaurantServicePort.findRestaurantById(id));
    }
}
