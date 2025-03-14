package com.pragma.plazoletaservice.application.handler.impl;

import com.pragma.plazoletaservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantClientResponseDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazoletaservice.application.mapper.IRestaurantRequestMapper;
import com.pragma.plazoletaservice.application.mapper.IRestaurantResponseMapper;
import com.pragma.plazoletaservice.domain.api.IRestaurantServicePort;
import com.pragma.plazoletaservice.domain.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantResponseMapper restaurantResponseMapper;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    private Restaurant restaurant;
    private RestaurantRequestDto restaurantRequestDto;
    private RestaurantResponseDto restaurantResponseDto;
    private List<Restaurant> restaurantList;
    private List<RestaurantResponseDto> restaurantResponseDtoList;
    private List<RestaurantClientResponseDto> restaurantClientResponseDtoList;

    @BeforeEach
    void setUp() {
        // Inicializar objetos para pruebas
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Test");
        restaurant.setAddress("Calle Test 123");
        restaurant.setPhone("+573001234567");
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setNit(123456789L);
        restaurant.setOwnerId(1L);

        restaurantRequestDto = new RestaurantRequestDto();
        restaurantRequestDto.setName("Restaurante Test");
        restaurantRequestDto.setAddress("Calle Test 123");
        restaurantRequestDto.setPhone("+573001234567");
        restaurantRequestDto.setUrlLogo("http://example.com/logo.png");
        restaurantRequestDto.setNit(123456789L);
        restaurantRequestDto.setOwnerId(1L);

        restaurantResponseDto = new RestaurantResponseDto();
        restaurantResponseDto.setId(1L);
        restaurantResponseDto.setName("Restaurante Test");
        restaurantResponseDto.setAddress("Calle Test 123");
        restaurantResponseDto.setPhone("+573001234567");
        restaurantResponseDto.setUrlLogo("http://example.com/logo.png");
        restaurantResponseDto.setNit(123456789L);
        restaurantResponseDto.setOwnerId(1L);

        restaurantList = Arrays.asList(restaurant);
        restaurantResponseDtoList = Arrays.asList(restaurantResponseDto);

        RestaurantClientResponseDto clientResponseDto = new RestaurantClientResponseDto();
        clientResponseDto.setName("Restaurante Test");
        clientResponseDto.setUrlLogo("http://example.com/logo.png");
        restaurantClientResponseDtoList = Arrays.asList(clientResponseDto);
    }

    @Test
    void saveRestaurant() {
        // Arrange
        when(restaurantRequestMapper.toRestaurant(any(RestaurantRequestDto.class))).thenReturn(restaurant);
        when(restaurantServicePort.saveRestaurant(any(Restaurant.class))).thenReturn(restaurant);
        when(restaurantResponseMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponseDto);

        // Act
        RestaurantResponseDto result = restaurantHandler.saveRestaurant(restaurantRequestDto);

        // Assert
        assertEquals(restaurantResponseDto, result);
        verify(restaurantRequestMapper).toRestaurant(restaurantRequestDto);
        verify(restaurantServicePort).saveRestaurant(restaurant);
        verify(restaurantResponseMapper).toResponse(restaurant);
    }

    @Test
    void findAllRestaurants() {
        // Arrange
        when(restaurantServicePort.findAllRestaurants()).thenReturn(restaurantList);
        when(restaurantResponseMapper.toResponseList(restaurantList)).thenReturn(restaurantResponseDtoList);

        // Act
        List<RestaurantResponseDto> result = restaurantHandler.findAllRestaurants();

        // Assert
        assertEquals(restaurantResponseDtoList, result);
        verify(restaurantServicePort).findAllRestaurants();
        verify(restaurantResponseMapper).toResponseList(restaurantList);
    }

    @Test
    void testFindAllRestaurants_WithPagination() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(restaurantServicePort.findAllRestaurants(pageable)).thenReturn(restaurantList);
        when(restaurantResponseMapper.toResponseClientList(restaurantList)).thenReturn(restaurantClientResponseDtoList);

        // Act
        List<RestaurantClientResponseDto> result = restaurantHandler.findAllRestaurants(pageable);

        // Assert
        assertEquals(restaurantClientResponseDtoList, result);
        verify(restaurantServicePort).findAllRestaurants(pageable);
        verify(restaurantResponseMapper).toResponseClientList(restaurantList);
    }

    @Test
    void findRestaurantById() {
        // Arrange
        Long restaurantId = 1L;
        when(restaurantServicePort.findRestaurantById(restaurantId)).thenReturn(restaurant);
        when(restaurantResponseMapper.toResponse(restaurant)).thenReturn(restaurantResponseDto);

        // Act
        RestaurantResponseDto result = restaurantHandler.findRestaurantById(restaurantId);

        // Assert
        assertEquals(restaurantResponseDto, result);
        verify(restaurantServicePort).findRestaurantById(restaurantId);
        verify(restaurantResponseMapper).toResponse(restaurant);
    }
}