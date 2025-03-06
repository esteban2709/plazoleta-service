package com.pragma.plazoletaservice.domain.usecase;

import com.pragma.plazoletaservice.domain.api.IRestaurantServicePort;
import com.pragma.plazoletaservice.domain.exception.CustomException;
import com.pragma.plazoletaservice.domain.exception.ExceptionMessage;
import com.pragma.plazoletaservice.domain.exception.InvalidFormatException;
import com.pragma.plazoletaservice.domain.model.Restaurant;
import com.pragma.plazoletaservice.domain.model.User;
import com.pragma.plazoletaservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoletaservice.domain.spi.IUserClientPort;

import java.util.List;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClientPort userClientPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserClientPort userClientPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClientPort = userClientPort;
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        // Validate NIT is numeric only
        if (!restaurant.getNit().toString().matches("^[0-9]+$")) {
            throw new InvalidFormatException(ExceptionMessage.INVALID_NIT_FORMAT.getMessage());
        }

        // Validate phone number format (max 13 characters, can contain '+')
        if (restaurant.getPhone().length() > 13 ||
                !restaurant.getPhone().matches("^\\+?[0-9]+$")) {
            throw new InvalidFormatException(ExceptionMessage.PHONE_INCORRECT_FORMAT.getMessage());
        }

        // Validate restaurant name is not numeric-only
        if (restaurant.getName().matches("^[0-9]+$")) {
            throw new InvalidFormatException(ExceptionMessage.INVALID_RESTAURANT_NAME.getMessage());
        }

        // Validate that the user exists and has the owner role
        User owner = userClientPort.getUserById(restaurant.getOwnerId());
        if (owner == null) {
            throw new CustomException(ExceptionMessage.USER_NOT_FOUND.getMessage());
        }

        if (!"OWNER".equals(owner.getRole().getName())) {
            throw new CustomException(ExceptionMessage.USER_NOT_OWNER.getMessage());
        }

        return restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> findAllRestaurants() {
        return restaurantPersistencePort.findAllRestaurants();
    }

    @Override
    public Restaurant findRestaurantById(Long id) {
        return restaurantPersistencePort.findRestaurantById(id);
    }
}
