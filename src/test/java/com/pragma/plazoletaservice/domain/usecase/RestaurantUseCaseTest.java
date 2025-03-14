package com.pragma.plazoletaservice.domain.usecase;

import com.pragma.plazoletaservice.domain.exception.CustomException;
import com.pragma.plazoletaservice.domain.exception.ExceptionMessage;
import com.pragma.plazoletaservice.domain.exception.InvalidFormatException;
import com.pragma.plazoletaservice.domain.model.Restaurant;
import com.pragma.plazoletaservice.domain.model.Role;
import com.pragma.plazoletaservice.domain.model.User;
import com.pragma.plazoletaservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoletaservice.domain.spi.IUserClientPort;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserClientPort userClientPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant restaurant;
    private User user;

    @BeforeEach
    void setUp() {
        // Configurar un objeto Restaurant con todos sus atributos
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Prueba");
        restaurant.setAddress("Calle 123 #45-67");
        restaurant.setPhone("+573001234567");
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setNit(123456789L);
        restaurant.setOwnerId(1L);

        // Configurar un objeto User con todos sus atributos
        Role role = new Role(1L, "OWNER", "OWNER");
        user = new User();
        user.setId(1L);
        user.setName("Esteban");
        user.setLastName("Gutierrez");
        user.setRole(role);
    }

    @Test
    void saveRestaurant() {
        // Arrange
        when(restaurantPersistencePort.saveRestaurant(any(Restaurant.class))).thenReturn(restaurant);
        when(userClientPort.getUserById(anyLong())).thenReturn(user);

        // Act
        Restaurant result = restaurantUseCase.saveRestaurant(restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Restaurante Prueba", result.getName());
        assertEquals("Calle 123 #45-67", result.getAddress());
        assertEquals("+573001234567", result.getPhone());
        assertEquals("http://example.com/logo.png", result.getUrlLogo());
        assertEquals(123456789, result.getNit());
        assertEquals(1L, result.getOwnerId());

        verify(restaurantPersistencePort).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void saveRestaurant_WithInvalidNitFormat_ThrowsException() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setNit(123456L);
        restaurant.setName("Restaurante Test");
        restaurant.setAddress("Calle 123");
        restaurant.setPhone("+573001234567");
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setOwnerId(1L);
        // Modificamos el NIT para que contenga caracteres no numéricos
        restaurant.setNit(null); // Simulamos un NIT inválido con null

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant));
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void saveRestaurant_WithInvalidPhoneFormat_ThrowsException() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setNit(123456L);
        restaurant.setName("Restaurante Test");
        restaurant.setAddress("Calle 123");
        restaurant.setPhone("123456789abcd"); // Teléfono con letras
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setOwnerId(1L);

        // Act & Assert
        InvalidFormatException exception = assertThrows(InvalidFormatException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant));
        assertEquals(ExceptionMessage.PHONE_INCORRECT_FORMAT.getMessage(), exception.getMessage());
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void saveRestaurant_WithPhoneTooLong_ThrowsException() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setNit(123456L);
        restaurant.setName("Restaurante Test");
        restaurant.setAddress("Calle 123");
        restaurant.setPhone("+5730012345678"); // 14 caracteres, más de 13
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setOwnerId(1L);

        // Act & Assert
        InvalidFormatException exception = assertThrows(InvalidFormatException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant));
        assertEquals(ExceptionMessage.PHONE_INCORRECT_FORMAT.getMessage(), exception.getMessage());
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void saveRestaurant_WithNumericOnlyName_ThrowsException() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setNit(123456L);
        restaurant.setName("12345"); // Nombre solo numérico
        restaurant.setAddress("Calle 123");
        restaurant.setPhone("+573001234567");
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setOwnerId(1L);

        // Act & Assert
        InvalidFormatException exception = assertThrows(InvalidFormatException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant));
        assertEquals(ExceptionMessage.INVALID_RESTAURANT_NAME.getMessage(), exception.getMessage());
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void saveRestaurant_WithNonExistentOwner_ThrowsException() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setNit(123456L);
        restaurant.setName("Restaurante Test");
        restaurant.setAddress("Calle 123");
        restaurant.setPhone("+573001234567");
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setOwnerId(1L);

        when(userClientPort.getUserById(anyLong())).thenReturn(null);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant));
        assertEquals(ExceptionMessage.USER_NOT_FOUND.getMessage(), exception.getMessage());

        verify(userClientPort).getUserById(1L);
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void saveRestaurant_WithNonOwnerRole_ThrowsException() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setNit(123456L);
        restaurant.setName("Restaurante Test");
        restaurant.setAddress("Calle 123");
        restaurant.setPhone("+573001234567");
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setOwnerId(1L);

        User user = new User();
        user.setId(1L);
        Role role = new Role();
        role.setId(2L);
        role.setName("CUSTOMER"); // Rol diferente a OWNER
        user.setRole(role);

        when(userClientPort.getUserById(anyLong())).thenReturn(user);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant));
        assertEquals(ExceptionMessage.USER_NOT_OWNER.getMessage(), exception.getMessage());

        verify(userClientPort).getUserById(1L);
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void saveRestaurant_WithValidData_Success() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setNit(123456L);
        restaurant.setName("Restaurante Test");
        restaurant.setAddress("Calle 123");
        restaurant.setPhone("+573001234567");
        restaurant.setUrlLogo("http://example.com/logo.png");
        restaurant.setOwnerId(1L);

        User user = new User();
        user.setId(1L);
        Role role = new Role();
        role.setId(1L);
        role.setName("OWNER");
        user.setRole(role);

        when(userClientPort.getUserById(anyLong())).thenReturn(user);
        when(restaurantPersistencePort.saveRestaurant(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        Restaurant result = restaurantUseCase.saveRestaurant(restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(123456L, result.getNit());
        assertEquals("Restaurante Test", result.getName());
        assertEquals("+573001234567", result.getPhone());
        assertEquals(1L, result.getOwnerId());

        verify(userClientPort).getUserById(1L);
        verify(restaurantPersistencePort).saveRestaurant(restaurant);
    }

    @Test
    void findAllRestaurants() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        Pageable pageable = PageRequest.of(0, 10);

        when(restaurantPersistencePort.findAllRestaurants(any(Pageable.class))).thenReturn(restaurants);

        // Act
        List<Restaurant> result = restaurantUseCase.findAllRestaurants(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Restaurante Prueba", result.get(0).getName());
        assertEquals("Calle 123 #45-67", result.get(0).getAddress());
        assertEquals("+573001234567", result.get(0).getPhone());
        assertEquals("http://example.com/logo.png", result.get(0).getUrlLogo());
        assertEquals(123456789, result.get(0).getNit());
        assertEquals(1L, result.get(0).getOwnerId());

        verify(restaurantPersistencePort).findAllRestaurants(pageable);
    }

    @Test
    void findAllRestaurantsTest() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);

        when(restaurantPersistencePort.findAllRestaurants()).thenReturn(restaurants);

        // Act
        List<Restaurant> result = restaurantUseCase.findAllRestaurants();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Restaurante Prueba", result.get(0).getName());
        assertEquals("Calle 123 #45-67", result.get(0).getAddress());
        assertEquals("+573001234567", result.get(0).getPhone());
        assertEquals("http://example.com/logo.png", result.get(0).getUrlLogo());
        assertEquals(123456789, result.get(0).getNit());
        assertEquals(1L, result.get(0).getOwnerId());

        verify(restaurantPersistencePort).findAllRestaurants();
    }

    @Test
    void findRestaurantById() {
        // Arrange
        when(restaurantPersistencePort.findRestaurantById(anyLong())).thenReturn(restaurant);

        // Act
        Restaurant result = restaurantUseCase.findRestaurantById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Restaurante Prueba", result.getName());
        assertEquals("Calle 123 #45-67", result.getAddress());
        assertEquals("+573001234567", result.getPhone());
        assertEquals("http://example.com/logo.png", result.getUrlLogo());
        assertEquals(123456789, result.getNit());
        assertEquals(1L, result.getOwnerId());

        verify(restaurantPersistencePort).findRestaurantById(1L);
    }

    @Test
    void findRestaurantById_ShouldReturnNull_WhenRestaurantDoesNotExist() {
        // Arrange
        when(restaurantPersistencePort.findRestaurantById(anyLong())).thenReturn(null);

        // Act
        Restaurant result = restaurantUseCase.findRestaurantById(999L);

        // Assert
        assertNull(result);
        verify(restaurantPersistencePort).findRestaurantById(999L);
    }

}