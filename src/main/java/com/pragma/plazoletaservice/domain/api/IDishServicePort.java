package com.pragma.plazoletaservice.domain.api;

import com.pragma.plazoletaservice.domain.model.Dish;

import java.util.List;

public interface IDishServicePort {

    Dish saveDish(Dish dish);

    Dish findDishById(Long id);

    Dish updateDish(Long id, Dish dish);

    Dish updateDishStatus(Long dishId, boolean active);

    void deleteDish(Long id);

    List<Dish> findAllDishes();
}
