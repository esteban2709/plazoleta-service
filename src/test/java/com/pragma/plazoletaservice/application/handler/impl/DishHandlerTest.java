package com.pragma.plazoletaservice.application.handler.impl;

import com.pragma.plazoletaservice.application.dto.request.DishRequestDto;
import com.pragma.plazoletaservice.application.dto.response.DishResponseDto;
import com.pragma.plazoletaservice.application.mapper.IDishRequestMapper;
import com.pragma.plazoletaservice.application.mapper.IDishResponseMapper;
import com.pragma.plazoletaservice.domain.api.IDishServicePort;
import com.pragma.plazoletaservice.domain.model.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishHandlerTest {

    @Mock
    private IDishServicePort dishServicePort;

    @Mock
    private IDishResponseMapper dishResponseMapper;

    @Mock
    private IDishRequestMapper dishRequestMapper;

    @InjectMocks
    private DishHandler dishHandler;

    private Dish dish;
    private DishRequestDto dishRequestDto;
    private DishResponseDto dishResponseDto;
    private List<Dish> dishList;
    private List<DishResponseDto> dishResponseDtoList;

    @BeforeEach
    void setUp() {
        // Inicializar objetos para pruebas
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setPrice(15000L);
        dish.setDescription("Deliciosa pizza");
        dish.setActive(true);

        dishRequestDto = new DishRequestDto();

        dishResponseDto = new DishResponseDto();
        dishResponseDto.setId(1L);
        dishResponseDto.setName("Pizza");

        dishList = List.of(dish);
        dishResponseDtoList = List.of(dishResponseDto);
    }

    @Test
    void saveDish() {
        // Arrange
        when(dishRequestMapper.toDish(dishRequestDto)).thenReturn(dish);
        when(dishServicePort.saveDish(dish)).thenReturn(dish);
        when(dishResponseMapper.toResponse(dish)).thenReturn(dishResponseDto);

        // Act
        DishResponseDto result = dishHandler.saveDish(dishRequestDto);

        // Assert
        assertEquals(dishResponseDto, result);
        verify(dishRequestMapper).toDish(dishRequestDto);
        verify(dishServicePort).saveDish(dish);
        verify(dishResponseMapper).toResponse(dish);
    }

    @Test
    void findDishById() {
        // Arrange
        when(dishServicePort.findDishById(1L)).thenReturn(dish);
        when(dishResponseMapper.toResponse(dish)).thenReturn(dishResponseDto);

        // Act
        DishResponseDto result = dishHandler.findDishById(1L);

        // Assert
        assertEquals(dishResponseDto, result);
        verify(dishServicePort).findDishById(1L);
        verify(dishResponseMapper).toResponse(dish);
    }

    @Test
    void updateDish() {
        // Arrange
        when(dishRequestMapper.toDish(dishRequestDto)).thenReturn(dish);
        when(dishServicePort.updateDish(eq(1L), any(Dish.class))).thenReturn(dish);
        when(dishResponseMapper.toResponse(dish)).thenReturn(dishResponseDto);

        // Act
        DishResponseDto result = dishHandler.updateDish(1L, dishRequestDto);

        // Assert
        assertEquals(dishResponseDto, result);
        verify(dishRequestMapper).toDish(dishRequestDto);
        verify(dishServicePort).updateDish(eq(1L), any(Dish.class));
        verify(dishResponseMapper).toResponse(dish);
    }

    @Test
    void updateDishStatus() {
        // Arrange
        boolean active = false;
        when(dishServicePort.updateDishStatus(1L, active)).thenReturn(dish);
        when(dishResponseMapper.toResponse(dish)).thenReturn(dishResponseDto);

        // Act
        DishResponseDto result = dishHandler.updateDishStatus(1L, active);

        // Assert
        assertEquals(dishResponseDto, result);
        verify(dishServicePort).updateDishStatus(1L, active);
        verify(dishResponseMapper).toResponse(dish);
    }

    @Test
    void deleteDish() {
        // Act
        dishHandler.deleteDish(1L);

        // Assert
        verify(dishServicePort).deleteDish(1L);
    }

    @Test
    void findAllDishes() {
        // Arrange
        when(dishServicePort.findAllDishes()).thenReturn(dishList);
        when(dishResponseMapper.toResponseList(dishList)).thenReturn(dishResponseDtoList);

        // Act
        List<DishResponseDto> result = dishHandler.findAllDishes();

        // Assert
        assertEquals(dishResponseDtoList, result);
        verify(dishServicePort).findAllDishes();
        verify(dishResponseMapper).toResponseList(dishList);
    }

    @Test
    void findAllDishesByRestaurantId() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(dishServicePort.findAllDishesByRestaurantId(1L, null, pageable)).thenReturn(dishList);
        when(dishResponseMapper.toResponseList(dishList)).thenReturn(dishResponseDtoList);

        // Act
        List<DishResponseDto> result = dishHandler.findAllDishesByRestaurantId(1L, pageable);

        // Assert
        assertEquals(dishResponseDtoList, result);
        verify(dishServicePort).findAllDishesByRestaurantId(1L, null, pageable);
        verify(dishResponseMapper).toResponseList(dishList);
    }

    @Test
    void findAllDishesByRestaurantIdAndCategoryId() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(dishServicePort.findAllDishesByRestaurantId(1L, 2L, pageable)).thenReturn(dishList);
        when(dishResponseMapper.toResponseList(dishList)).thenReturn(dishResponseDtoList);

        // Act
        List<DishResponseDto> result = dishHandler.findAllDishesByRestaurantIdAndCategoryId(1L, 2L, pageable);

        // Assert
        assertEquals(dishResponseDtoList, result);
        verify(dishServicePort).findAllDishesByRestaurantId(1L, 2L, pageable);
        verify(dishResponseMapper).toResponseList(dishList);
    }
}