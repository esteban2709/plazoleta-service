package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.domain.model.Dish;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDishPersistencePort {
    Dish saveDish(Dish dish);

    Dish findDishById(Long id);

    List<Dish> findAllDishes();

    void deleteDish(Long id);

    List<Dish> findAllDishesByRestaurantId(Long restaurantId, Long categoryId, Pageable pageable);
}
