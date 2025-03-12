package com.pragma.plazoletaservice.infraestructure.configuration;

import com.pragma.plazoletaservice.domain.api.IDishServicePort;
import com.pragma.plazoletaservice.domain.api.IOrderServicePort;
import com.pragma.plazoletaservice.domain.api.IRestaurantServicePort;
import com.pragma.plazoletaservice.domain.spi.*;
import com.pragma.plazoletaservice.domain.usecase.DishUseCase;
import com.pragma.plazoletaservice.domain.usecase.OrderUseCase;
import com.pragma.plazoletaservice.domain.usecase.RestaurantUseCase;
import com.pragma.plazoletaservice.infraestructure.out.clients.MessageClient;
import com.pragma.plazoletaservice.infraestructure.out.clients.adapter.MessagingClientAdapter;
import com.pragma.plazoletaservice.infraestructure.out.clients.adapter.UserClientAdapter;
import com.pragma.plazoletaservice.infraestructure.out.clients.UserClient;
import com.pragma.plazoletaservice.infraestructure.out.clients.mapper.IMessageDtoMapper;
import com.pragma.plazoletaservice.infraestructure.out.clients.mapper.IUserDtoMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.adapter.*;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.*;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.*;
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
    private final MessageClient messageClient;
    private final IMessageDtoMapper messageDtoMapper;

    //Dish
    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    //Category
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    //Order
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    private final IOrderDishRepository orderDishRepository;
    private final IOrderDishEntityMapper orderDishEntityMapper;

    @Bean
    public IOrderDishPersistencePort orderDishPersistencePort() {
        return new OrderDishJpaAdapter(orderDishRepository, orderDishEntityMapper);
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort() {
        return new OrderJpaAdapter(orderRepository, orderEntityMapper, restaurantRepository);
    }

    @Bean
    public IOrderServicePort orderServicePort() {
        return new OrderUseCase(orderPersistencePort(), orderDishPersistencePort(), userClientPort(),
                tokenUtilsPort(), messagingClientPort());
    }

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
    public IMessagingClientPort messagingClientPort() {
        return new MessagingClientAdapter(messageClient, messageDtoMapper);
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
