package com.pragma.plazoletaservice.domain.usecase;

import com.pragma.plazoletaservice.domain.exception.CustomException;
import com.pragma.plazoletaservice.domain.model.Category;
import com.pragma.plazoletaservice.domain.model.Dish;
import com.pragma.plazoletaservice.domain.model.Restaurant;
import com.pragma.plazoletaservice.domain.spi.ICategoryPersistencePort;
import com.pragma.plazoletaservice.domain.spi.IDishPersistencePort;
import com.pragma.plazoletaservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoletaservice.domain.spi.ITokenUtilsPort;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @Mock
    private ITokenUtilsPort tokenUtilsPort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Dish dish;
    private Restaurant restaurant;
    private Category category;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        // Configurar restaurant
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Test");
        restaurant.setAddress("Calle Test 123");
        restaurant.setPhone("+573001234567");
        restaurant.setUrlLogo("http://test.com/logo.png");
        restaurant.setNit(123456789L);
        restaurant.setOwnerId(userId);

        // Configurar category
        category = new Category();
        category.setId(1L);
        category.setName("Categoría Test");
        category.setDescription("Descripción de categoría");

        // Configurar dish
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Plato Test");
        dish.setDescription("Descripción del plato");
        dish.setPrice(15000L);
        dish.setUrlImage("http://test.com/image.jpg");
        dish.setActive(true);
        dish.setRestaurant(restaurant);
        dish.setCategory(category);
    }

    @Test
    void saveDish_Success() {
        // Arrange
        when(restaurantPersistencePort.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(tokenUtilsPort.getUserId()).thenReturn(userId);
        when(dishPersistencePort.saveDish(any(Dish.class))).thenReturn(dish);

        // Act
        Dish savedDish = dishUseCase.saveDish(dish);

        // Assert
        assertNotNull(savedDish);
        assertEquals(dish.getId(), savedDish.getId());
        assertEquals(dish.getName(), savedDish.getName());
        assertEquals(dish.getDescription(), savedDish.getDescription());
        assertEquals(dish.getPrice(), savedDish.getPrice());
        assertEquals(dish.getUrlImage(), savedDish.getUrlImage());
        assertEquals(dish.getActive(), savedDish.getActive());
        assertEquals(dish.getCategory(), savedDish.getCategory());
        assertEquals(dish.getRestaurant(), savedDish.getRestaurant());

        verify(dishPersistencePort).saveDish(dish);
        verify(restaurantPersistencePort).findRestaurantById(dish.getRestaurant().getId());
        verify(tokenUtilsPort).getUserId();
    }

    @Test
    void saveDish_NullCategory_ThrowsException() {
        // Arrange
        dish.setCategory(null);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            dishUseCase.saveDish(dish);
        });

        assertEquals("No category was found for the requested operation", exception.getMessage());
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void saveDish_NullRestaurant_ThrowsException() {
        // Arrange
        dish.setRestaurant(null);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            dishUseCase.saveDish(dish);
        });

        assertEquals("No restaurant was found for the requested operation", exception.getMessage());
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void saveDish_NotOwner_ThrowsException() {
        // Arrange
        when(restaurantPersistencePort.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(tokenUtilsPort.getUserId()).thenReturn(2L); // Usuario diferente al dueño

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            dishUseCase.saveDish(dish);
        });

        assertEquals("User is not the owner of the restaurant", exception.getMessage());
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void findDishById_Success() {
        // Arrange
        when(dishPersistencePort.findDishById(anyLong())).thenReturn(dish);

        // Act
        Dish foundDish = dishUseCase.findDishById(1L);

        // Assert
        assertNotNull(foundDish);
        assertEquals(dish.getId(), foundDish.getId());
        assertEquals(dish.getName(), foundDish.getName());
        assertEquals(dish.getDescription(), foundDish.getDescription());
        assertEquals(dish.getPrice(), foundDish.getPrice());
        assertEquals(dish.getUrlImage(), foundDish.getUrlImage());
        assertEquals(dish.getActive(), foundDish.getActive());
        assertEquals(dish.getCategory(), foundDish.getCategory());
        assertEquals(dish.getRestaurant(), foundDish.getRestaurant());

        verify(dishPersistencePort).findDishById(1L);
    }

    @Test
    void updateDish_Success() {
        // Arrange
        Dish updatedDish = new Dish();
        updatedDish.setPrice(20000L);
        updatedDish.setDescription("Descripción actualizada");

        when(dishPersistencePort.findDishById(anyLong())).thenReturn(dish);
        when(dishPersistencePort.saveDish(any(Dish.class))).thenReturn(dish);

        // Act
        Dish result = dishUseCase.updateDish(1L, updatedDish);

        // Assert
        assertNotNull(result);
        assertEquals(20000, result.getPrice());
        assertEquals("Descripción actualizada", result.getDescription());

        verify(dishPersistencePort).findDishById(1L);
        verify(dishPersistencePort).saveDish(any(Dish.class));
    }

    @Test
    void updateDishStatus_Success() {
        // Arrange
        when(dishPersistencePort.findDishById(anyLong())).thenReturn(dish);
        when(restaurantPersistencePort.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(tokenUtilsPort.getUserId()).thenReturn(userId);
        when(dishPersistencePort.saveDish(any(Dish.class))).thenReturn(dish);

        // Act
        Dish result = dishUseCase.updateDishStatus(1L, false);

        // Assert
        assertNotNull(result);
        assertFalse(result.getActive());

        verify(dishPersistencePort).findDishById(1L);
        verify(restaurantPersistencePort).findRestaurantById(dish.getRestaurant().getId());
        verify(tokenUtilsPort).getUserId();
        verify(dishPersistencePort).saveDish(dish);
    }

    @Test
    void updateDishStatus_DishNotFound_ThrowsException() {
        // Arrange
        when(dishPersistencePort.findDishById(anyLong())).thenReturn(null);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            dishUseCase.updateDishStatus(1L, false);
        });

        assertEquals("No dish was found for the requested operation", exception.getMessage());
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void updateDishStatus_NotOwner_ThrowsException() {
        // Arrange
        when(dishPersistencePort.findDishById(anyLong())).thenReturn(dish);
        when(restaurantPersistencePort.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(tokenUtilsPort.getUserId()).thenReturn(2L); // Usuario diferente al dueño

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            dishUseCase.updateDishStatus(1L, false);
        });

        assertEquals("User is not the owner of the restaurant", exception.getMessage());
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void deleteDish_Success() {
        // Arrange
        doNothing().when(dishPersistencePort).deleteDish(anyLong());

        // Act
        dishUseCase.deleteDish(1L);

        // Assert
        verify(dishPersistencePort).deleteDish(1L);
    }

    @Test
    void findAllDishes_Success() {
        // Arrange
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Plato Test 2");
        dish2.setDescription("Descripción del plato 2");
        dish2.setPrice(25000L);
        dish2.setUrlImage("http://test.com/image2.jpg");
        dish2.setActive(true);
        dish2.setRestaurant(restaurant);
        dish2.setCategory(category);

        List<Dish> expectedDishes = Arrays.asList(dish, dish2);
        when(dishPersistencePort.findAllDishes()).thenReturn(expectedDishes);

        // Act
        List<Dish> dishes = dishUseCase.findAllDishes();

        // Assert
        assertNotNull(dishes);
        assertEquals(2, dishes.size());
        assertEquals(dish.getId(), dishes.get(0).getId());
        assertEquals(dish2.getId(), dishes.get(1).getId());

        verify(dishPersistencePort).findAllDishes();
    }

    @Test
    void findAllDishesByRestaurantId_Success() {
        // Arrange
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Plato Test 2");
        dish2.setDescription("Descripción del plato 2");
        dish2.setPrice(25000L);
        dish2.setUrlImage("http://test.com/image2.jpg");
        dish2.setActive(true);
        dish2.setRestaurant(restaurant);
        dish2.setCategory(category);

        List<Dish> expectedDishes = Arrays.asList(dish, dish2);
        Pageable pageable = PageRequest.of(0, 10);
        when(dishPersistencePort.findAllDishesByRestaurantId(anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(expectedDishes);

        // Act
        List<Dish> dishes = dishUseCase.findAllDishesByRestaurantId(1L, 1L, pageable);

        // Assert
        assertNotNull(dishes);
        assertEquals(2, dishes.size());
        assertEquals(dish.getId(), dishes.get(0).getId());
        assertEquals(dish2.getId(), dishes.get(1).getId());

        verify(dishPersistencePort).findAllDishesByRestaurantId(1L, 1L, pageable);
    }
}