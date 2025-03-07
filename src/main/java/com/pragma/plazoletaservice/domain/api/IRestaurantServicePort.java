package com.pragma.plazoletaservice.domain.api;

import com.pragma.plazoletaservice.domain.model.Restaurant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantServicePort {
    Restaurant saveRestaurant(Restaurant restaurant);

    List<Restaurant> findAllRestaurants();

    List<Restaurant> findAllRestaurants(Pageable pageable);

    Restaurant findRestaurantById(Long id);
}
