package com.pragma.plazoletaservice.domain.api;

import com.pragma.plazoletaservice.domain.model.Dish;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDishServicePort {

    Dish saveDish(Dish dish);

    Dish findDishById(Long id);

    Dish updateDish(Long id, Dish dish);

    Dish updateDishStatus(Long dishId, boolean active);

    void deleteDish(Long id);

    List<Dish> findAllDishes();

    List<Dish> findAllDishesByRestaurantId(Long restaurantId, Long categoryId, Pageable pageable);
}
