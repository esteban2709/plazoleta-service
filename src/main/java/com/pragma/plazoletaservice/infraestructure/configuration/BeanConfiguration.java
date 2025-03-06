package com.pragma.plazoletaservice.infraestructure.configuration;

import com.pragma.plazoletaservice.domain.api.IDishServicePort;
import com.pragma.plazoletaservice.domain.api.IRestaurantServicePort;
import com.pragma.plazoletaservice.domain.spi.*;
import com.pragma.plazoletaservice.domain.usecase.DishUseCase;
import com.pragma.plazoletaservice.domain.usecase.RestaurantUseCase;
import com.pragma.plazoletaservice.infraestructure.out.clients.adapter.UserClientAdapter;
import com.pragma.plazoletaservice.infraestructure.out.clients.UserClient;
import com.pragma.plazoletaservice.infraestructure.out.clients.mapper.IUserDtoMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.adapter.CategoryJpaAdapter;
import com.pragma.plazoletaservice.infraestructure.out.jpa.adapter.DishJpaAdapter;
import com.pragma.plazoletaservice.infraestructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.ICategoryEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.ICategoryRepository;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.IDishRepository;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.IRestaurantRepository;
import com.pragma.plazoletaservice.infraestructure.out.utils.TokenUtilsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    //Restaurant
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final UserClient userClient;
    private final IUserDtoMapper userDtoMapper;

    //Dish
    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    //Category
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Bean
    public ICategoryPersistencePort categoryPersistencePort() {
        return new CategoryJpaAdapter(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(), categoryPersistencePort(), tokenUtilsPort(), restaurantPersistencePort());
    }

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IUserClientPort userClientPort() {
        return new UserClientAdapter(userClient, userDtoMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userClientPort());
    }

    @Bean
    public ITokenUtilsPort tokenUtilsPort() {
        return new TokenUtilsAdapter();
    }
}
