package com.pragma.plazoletaservice.domain.api;

import com.pragma.plazoletaservice.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {
    Restaurant saveRestaurant(Restaurant restaurant);

    List<Restaurant> findAllRestaurants();

    Restaurant findRestaurantById(Long id);
}
