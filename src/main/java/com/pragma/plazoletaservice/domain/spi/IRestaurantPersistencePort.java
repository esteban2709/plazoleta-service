package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantPersistencePort {
    Restaurant saveRestaurant(Restaurant restaurant);

    Restaurant findRestaurantById(Long id);

    List<Restaurant> findAllRestaurants();

}
