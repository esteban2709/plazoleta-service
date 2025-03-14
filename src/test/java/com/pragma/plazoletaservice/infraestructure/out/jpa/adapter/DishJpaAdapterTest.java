package com.pragma.plazoletaservice.infraestructure.out.jpa.adapter;

import com.pragma.plazoletaservice.domain.model.Category;
import com.pragma.plazoletaservice.domain.model.Dish;
import com.pragma.plazoletaservice.domain.model.Restaurant;
import com.pragma.plazoletaservice.infraestructure.exception.NoDataFoundException;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.CategoryEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.DishEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.RestaurantEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.IDishRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishJpaAdapterTest {

    @Mock
    private IDishRepository dishRepository;

    @Mock
    private IDishEntityMapper dishEntityMapper;

    @InjectMocks
    private DishJpaAdapter dishJpaAdapter;

    private Dish dish;
    private DishEntity dishEntity;
    private List<Dish> dishes;
    private List<DishEntity> dishEntities;

    @BeforeEach
    void setUp() {
        // Configurar plato de ejemplo
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Plato de prueba");
        dish.setDescription("Descripción del plato");
        dish.setPrice(15000L);
        dish.setUrlImage("http://example.com/imagen.jpg");
        dish.setActive(true);

        Category category = new Category();
        category.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante de prueba");

        category.setName("Categoría de prueba");
        dish.setCategory(category);
        dish.setRestaurant(restaurant);

        // Configurar entidad de plato

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Categoría de prueba");

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Restaurante de prueba");

        dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setName("Plato de prueba");
        dishEntity.setDescription("Descripción del plato");
        dishEntity.setPrice(15000L);
        dishEntity.setUrlImage("http://example.com/imagen.jpg");
        dishEntity.setActive(true);
        dishEntity.setCategory(categoryEntity);
        dishEntity.setRestaurant(restaurantEntity);

        // Configurar listas
        dishes = Arrays.asList(dish);
        dishEntities = Arrays.asList(dishEntity);
    }

    @Test
    void saveDish_Success() {
        // Arrange
        when(dishEntityMapper.toDishEntity(any(Dish.class))).thenReturn(dishEntity);
        when(dishRepository.save(any(DishEntity.class))).thenReturn(dishEntity);
        when(dishEntityMapper.toDish(any(DishEntity.class))).thenReturn(dish);

        // Act
        Dish result = dishJpaAdapter.saveDish(dish);

        // Assert
        assertNotNull(result);
        assertEquals(dish.getId(), result.getId());
        assertEquals(dish.getName(), result.getName());
        verify(dishEntityMapper).toDishEntity(dish);
        verify(dishRepository).save(dishEntity);
        verify(dishEntityMapper).toDish(dishEntity);
    }

    @Test
    void findDishById_Success() {
        // Arrange
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dishEntity));
        when(dishEntityMapper.toDish(any(DishEntity.class))).thenReturn(dish);

        // Act
        Dish result = dishJpaAdapter.findDishById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(dish.getId(), result.getId());
        verify(dishRepository).findById(1L);
        verify(dishEntityMapper).toDish(dishEntity);
    }

    @Test
    void findDishById_NotFound() {
        // Arrange
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Dish result = dishJpaAdapter.findDishById(1L);

        // Assert
        assertNull(result);
        verify(dishRepository).findById(1L);
        verify(dishEntityMapper, never()).toDish(any());
    }

    @Test
    void findAllDishes_Success() {
        // Arrange
        when(dishRepository.findAll()).thenReturn(dishEntities);
        when(dishEntityMapper.toDishList(anyList())).thenReturn(dishes);

        // Act
        List<Dish> result = dishJpaAdapter.findAllDishes();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(dishes.size(), result.size());
        verify(dishRepository).findAll();
        verify(dishEntityMapper).toDishList(dishEntities);
    }

    @Test
    void findAllDishes_EmptyList() {
        // Arrange
        when(dishRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoDataFoundException.class, () -> dishJpaAdapter.findAllDishes());
        verify(dishRepository).findAll();
        verify(dishEntityMapper, never()).toDishList(anyList());
    }

    @Test
    void deleteDish_Success() {
        // Arrange
        doNothing().when(dishRepository).deleteById(anyLong());

        // Act
        dishJpaAdapter.deleteDish(1L);

        // Assert
        verify(dishRepository).deleteById(1L);
    }

    @Test
    void findAllDishesByRestaurantId_WithoutCategory_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DishEntity> page = new PageImpl<>(dishEntities);
        when(dishRepository.findAllByRestaurantId(anyLong(), any(Pageable.class))).thenReturn(page);
        when(dishEntityMapper.toDishList(anyList())).thenReturn(dishes);

        // Act
        List<Dish> result = dishJpaAdapter.findAllDishesByRestaurantId(1L, null, pageable);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(dishes.size(), result.size());
        verify(dishRepository).findAllByRestaurantId(eq(1L), eq(pageable));
        verify(dishEntityMapper).toDishList(dishEntities);
    }

    @Test
    void findAllDishesByRestaurantId_WithCategory_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DishEntity> page = new PageImpl<>(dishEntities);
        when(dishRepository.findAllByRestaurantIdAndCategoryId(anyLong(), anyLong(), any(Pageable.class))).thenReturn(page);
        when(dishEntityMapper.toDishList(anyList())).thenReturn(dishes);

        // Act
        List<Dish> result = dishJpaAdapter.findAllDishesByRestaurantId(1L, 1L, pageable);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(dishes.size(), result.size());
        verify(dishRepository).findAllByRestaurantIdAndCategoryId(eq(1L), eq(1L), eq(pageable));
        verify(dishEntityMapper).toDishList(dishEntities);
    }

    @Test
    void findAllDishesByRestaurantId_EmptyList() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DishEntity> page = new PageImpl<>(new ArrayList<>());
        when(dishRepository.findAllByRestaurantId(anyLong(), any(Pageable.class))).thenReturn(page);

        // Act & Assert
        assertThrows(NoDataFoundException.class, () -> dishJpaAdapter.findAllDishesByRestaurantId(1L, null, pageable));
        verify(dishRepository).findAllByRestaurantId(eq(1L), eq(pageable));
        verify(dishEntityMapper, never()).toDishList(anyList());
    }
}