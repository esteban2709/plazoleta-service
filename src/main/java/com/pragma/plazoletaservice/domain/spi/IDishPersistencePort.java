package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.domain.model.Dish;

import java.util.List;

public interface IDishPersistencePort {
    Dish saveDish(Dish dish);

    Dish findDishById(Long id);

    List<Dish> findAllDishes();

    void deleteDish(Long id);
}
