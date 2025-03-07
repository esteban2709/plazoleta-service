package com.pragma.plazoletaservice.infraestructure.out.jpa.adapter;

import com.pragma.plazoletaservice.domain.model.Restaurant;
import com.pragma.plazoletaservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoletaservice.infraestructure.exception.NoDataFoundException;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.RestaurantEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;

private final IRestaurantEntityMapper restaurantEntityMapper;
    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurant);
        return restaurantEntityMapper.toRestaurant(restaurantRepository.save(restaurantEntity));
    }

    @Override
    public Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id).map(restaurantEntityMapper::toRestaurant).orElse(null);
    }

    @Override
    public List<Restaurant> findAllRestaurants() {
        List<RestaurantEntity> entityList = restaurantRepository.findAll();
        if (entityList.isEmpty()) {
            throw new NoDataFoundException();
        }
        return restaurantEntityMapper.toRestaurants(entityList);
    }

    @Override
    public List<Restaurant> findAllRestaurants(Pageable pageable) {
        List<RestaurantEntity> entityList = restaurantRepository.findAll(pageable).getContent();
        if (entityList.isEmpty()) {
            throw new NoDataFoundException();
        }
        return restaurantEntityMapper.toRestaurants(entityList);
    }
}
