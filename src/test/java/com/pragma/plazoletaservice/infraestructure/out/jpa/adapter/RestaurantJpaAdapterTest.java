package com.pragma.plazoletaservice.infraestructure.out.jpa.adapter;

import com.pragma.plazoletaservice.domain.model.Restaurant;
import com.pragma.plazoletaservice.infraestructure.exception.NoDataFoundException;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.RestaurantEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantJpaAdapterTest {

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private IRestaurantEntityMapper restaurantEntityMapper;

    @InjectMocks
    private RestaurantJpaAdapter restaurantJpaAdapter;

    private Restaurant restaurant;
    private RestaurantEntity restaurantEntity;
    private List<Restaurant> restaurants;
    private List<RestaurantEntity> restaurantEntities;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Prueba");
        restaurant.setAddress("Calle 123 #45-67");
        restaurant.setPhone("+573001234567");
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setNit(123456789L);
        restaurant.setOwnerId(1L);

        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Restaurante Prueba");
        restaurantEntity.setAddress("Calle 123 #45-67");
        restaurantEntity.setPhone("+573001234567");
        restaurantEntity.setUrlLogo("http://example.com/logo.png");
        restaurantEntity.setNit(123456789L);
        restaurantEntity.setOwnerId(1L);

        restaurants = new ArrayList<>();
        restaurants.add(restaurant);

        restaurantEntities = new ArrayList<>();
        restaurantEntities.add(restaurantEntity);
    }

    @Test
    void saveRestaurant_Success() {
        // Arrange
        when(restaurantEntityMapper.toEntity(any(Restaurant.class))).thenReturn(restaurantEntity);
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);
        when(restaurantEntityMapper.toRestaurant(any(RestaurantEntity.class))).thenReturn(restaurant);

        // Act
        Restaurant result = restaurantJpaAdapter.saveRestaurant(restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(restaurant.getId(), result.getId());
        assertEquals(restaurant.getName(), result.getName());
        assertEquals(restaurant.getAddress(), result.getAddress());
        verify(restaurantEntityMapper).toEntity(restaurant);
        verify(restaurantRepository).save(restaurantEntity);
        verify(restaurantEntityMapper).toRestaurant(restaurantEntity);
    }

    @Test
    void findRestaurantById_Success() {
        // Arrange
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.toRestaurant(any(RestaurantEntity.class))).thenReturn(restaurant);

        // Act
        Restaurant result = restaurantJpaAdapter.findRestaurantById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(restaurant.getId(), result.getId());
        assertEquals(restaurant.getName(), result.getName());
        verify(restaurantRepository).findById(1L);
        verify(restaurantEntityMapper).toRestaurant(restaurantEntity);
    }

    @Test
    void findRestaurantById_NotFound() {
        // Arrange
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Restaurant result = restaurantJpaAdapter.findRestaurantById(1L);

        // Assert
        assertNull(result);
        verify(restaurantRepository).findById(1L);
        verify(restaurantEntityMapper, never()).toRestaurant(any());
    }

    @Test
    void findAllRestaurants_Success() {
        // Arrange
        when(restaurantRepository.findAll()).thenReturn(restaurantEntities);
        when(restaurantEntityMapper.toRestaurants(anyList())).thenReturn(restaurants);

        // Act
        List<Restaurant> result = restaurantJpaAdapter.findAllRestaurants();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(restaurants.size(), result.size());
        verify(restaurantRepository).findAll();
        verify(restaurantEntityMapper).toRestaurants(restaurantEntities);
    }

    @Test
    void findAllRestaurants_EmptyList() {
        // Arrange
        when(restaurantRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoDataFoundException.class, () -> restaurantJpaAdapter.findAllRestaurants());
        verify(restaurantRepository).findAll();
        verify(restaurantEntityMapper, never()).toRestaurants(anyList());
    }

    @Test
    void findAllRestaurantsWithPageable_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<RestaurantEntity> page = new PageImpl<>(restaurantEntities);
        when(restaurantRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(restaurantEntityMapper.toRestaurants(anyList())).thenReturn(restaurants);

        // Act
        List<Restaurant> result = restaurantJpaAdapter.findAllRestaurants(pageable);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(restaurants.size(), result.size());
        verify(restaurantRepository).findAll(pageable);
        verify(restaurantEntityMapper).toRestaurants(restaurantEntities);
    }

    @Test
    void findAllRestaurantsWithPageable_EmptyList() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<RestaurantEntity> page = new PageImpl<>(new ArrayList<>());
        when(restaurantRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act & Assert
        assertThrows(NoDataFoundException.class, () -> restaurantJpaAdapter.findAllRestaurants(pageable));
        verify(restaurantRepository).findAll(pageable);
        verify(restaurantEntityMapper, never()).toRestaurants(anyList());
    }
}