package com.pragma.plazoletaservice.domain.usecase;

import com.pragma.plazoletaservice.domain.api.IDishServicePort;
import com.pragma.plazoletaservice.domain.exception.CustomException;
import com.pragma.plazoletaservice.domain.exception.ExceptionMessage;
import com.pragma.plazoletaservice.domain.model.Dish;
import com.pragma.plazoletaservice.domain.model.Restaurant;
import com.pragma.plazoletaservice.domain.spi.ICategoryPersistencePort;
import com.pragma.plazoletaservice.domain.spi.IDishPersistencePort;
import com.pragma.plazoletaservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoletaservice.domain.spi.ITokenUtilsPort;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Objects;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final ITokenUtilsPort tokenUtilsPort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, ICategoryPersistencePort categoryPersistencePort, ITokenUtilsPort tokenUtilsPort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.tokenUtilsPort = tokenUtilsPort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public Dish saveDish(Dish dish) {
        if (dish.getCategory() == null) {
            throw new CustomException(ExceptionMessage.CATEGORY_NOT_FOUND.getMessage());
        }
        if (dish.getRestaurant() == null) {
            throw new CustomException(ExceptionMessage.RESTAURANT_NOT_FOUND.getMessage());
        }
        verifyOwnership(dish);

        return dishPersistencePort.saveDish(dish);
    }

    @Override
    public Dish findDishById(Long id) {
        return dishPersistencePort.findDishById(id);
    }

    @Override
    public Dish updateDish(Long id, Dish dish) {
        Dish dishToUpdate = dishPersistencePort.findDishById(id);
        dishToUpdate.setPrice(dish.getPrice());
        dishToUpdate.setDescription(dish.getDescription());
        return dishPersistencePort.saveDish( dishToUpdate);
    }

    @Override
    public Dish updateDishStatus(Long dishId, boolean active) {
        // Get current dish
        Dish dish = dishPersistencePort.findDishById(dishId);
        if (dish == null) {
            throw new CustomException(ExceptionMessage.DISH_NOT_FOUND.getMessage());
        }

        verifyOwnership(dish);

        // Update dish status
        dish.setActive(active);
        return dishPersistencePort.saveDish(dish);
    }

    private void verifyOwnership(Dish dish) {
        // Get current authenticated user
        Long currentUserId = tokenUtilsPort.getUserId();

        // Get restaurant to verify ownership
        Restaurant restaurant = restaurantPersistencePort.findRestaurantById(dish.getRestaurant().getId());
        if (restaurant == null) {
            throw new CustomException(ExceptionMessage.RESTAURANT_NOT_FOUND.getMessage());
        }

        // Verify user is the owner of the restaurant
        if ((!Objects.equals(restaurant.getOwnerId(), currentUserId))) {
            throw new CustomException(ExceptionMessage.NOT_RESTAURANT_OWNER.getMessage());
        }
    }

    @Override
    public void deleteDish(Long id) {
        dishPersistencePort.deleteDish(id);
    }

    @Override
    public List<Dish> findAllDishes() {
        return dishPersistencePort.findAllDishes();
    }

    @Override
    public List<Dish> findAllDishesByRestaurantId(Long restaurantId, Long categoryId, Pageable pageable) {
        return dishPersistencePort.findAllDishesByRestaurantId(restaurantId, categoryId, pageable);
    }
}
