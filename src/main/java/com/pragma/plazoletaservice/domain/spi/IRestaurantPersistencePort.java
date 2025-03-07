package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.domain.model.Restaurant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantPersistencePort {
    Restaurant saveRestaurant(Restaurant restaurant);

    Restaurant findRestaurantById(Long id);

    List<Restaurant> findAllRestaurants();

    List<Restaurant> findAllRestaurants(Pageable pageable);
}
