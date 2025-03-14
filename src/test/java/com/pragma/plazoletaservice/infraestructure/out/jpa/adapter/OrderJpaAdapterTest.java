package com.pragma.plazoletaservice.infraestructure.out.jpa.adapter;

import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.domain.model.Order;
import com.pragma.plazoletaservice.domain.model.Restaurant;
import com.pragma.plazoletaservice.domain.model.User;
import com.pragma.plazoletaservice.infraestructure.exception.NoDataFoundException;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.OrderEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.RestaurantEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.IOrderRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderJpaAdapterTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderEntityMapper orderEntityMapper;

    @Mock
    private IRestaurantRepository restaurantRepository;

    @InjectMocks
    private OrderJpaAdapter orderJpaAdapter;

    private OrderEntity orderEntity;
    private Order order;
    private RestaurantEntity restaurantEntity;
    private Restaurant restaurant;
    private List<OrderEntity> orderEntities;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        // Inicialización de datos de prueba
        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Restaurante Prueba");

        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Prueba");

        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        orderEntity.setRestaurant(restaurantEntity);
        orderEntity.setClientId(1L);

        order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setRestaurant(restaurant);
        User client = new User();
        client.setId(1L);
        order.setClient(client);

        orderEntities = new ArrayList<>();
        orderEntities.add(orderEntity);

        orders = new ArrayList<>();
        orders.add(order);
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(orderEntityMapper.toEntity(any(Order.class))).thenReturn(orderEntity);
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurantEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(orderEntityMapper.toOrder(any(OrderEntity.class))).thenReturn(order);

        // Act
        Order result = orderJpaAdapter.createOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderStatus(), result.getOrderStatus());
        verify(orderRepository).save(any(OrderEntity.class));
        verify(restaurantRepository).findById(anyLong());
    }

    @Test
    void createOrder_NoRestaurant() {
        // Arrange
        order.setRestaurant(null);
        when(orderEntityMapper.toEntity(any(Order.class))).thenReturn(orderEntity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(orderEntityMapper.toOrder(any(OrderEntity.class))).thenReturn(order);

        // Act
        Order result = orderJpaAdapter.createOrder(order);

        // Assert
        assertNotNull(result);
        verify(orderRepository).save(any(OrderEntity.class));
        verify(restaurantRepository, never()).findById(anyLong());
    }

    @Test
    void findOrderById_Success() {
        // Arrange
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(orderEntityMapper.toOrder(any(OrderEntity.class))).thenReturn(order);

        // Act
        Order result = orderJpaAdapter.findOrderById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository).findById(1L);
    }

    @Test
    void findOrderById_NotFound() {
        // Arrange
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(orderEntityMapper.toOrder(any())).thenReturn(null);

        // Act
        Order result = orderJpaAdapter.findOrderById(1L);

        // Assert
        assertNull(result);
        verify(orderRepository).findById(1L);
    }

    @Test
    void updateOrder_Success() {
        // Arrange
        when(orderEntityMapper.toEntity(any(Order.class))).thenReturn(orderEntity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(orderEntityMapper.toOrder(any(OrderEntity.class))).thenReturn(order);

        // Act
        Order result = orderJpaAdapter.updateOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository).save(any(OrderEntity.class));
    }

    @Test
    void deleteOrder_Success() {
        // Arrange
        doNothing().when(orderRepository).deleteById(anyLong());

        // Act
        orderJpaAdapter.deleteOrder(1L);

        // Assert
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void findAllOrders_Success() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(orderEntities);
        when(orderEntityMapper.toOrderList(anyList())).thenReturn(orders);

        // Act
        List<Order> result = orderJpaAdapter.findAllOrders();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(orders.size(), result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void findAllOrders_EmptyList() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoDataFoundException.class, () -> orderJpaAdapter.findAllOrders());
        verify(orderRepository).findAll();
    }

    @Test
    void findAllOrdersByUserId_Success() {
        // Arrange
        when(orderRepository.findByClientIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(orderEntities);
        when(orderEntityMapper.toOrderList(anyList())).thenReturn(orders);

        // Act
        List<Order> result = orderJpaAdapter.findAllOrdersByUserId(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(orders.size(), result.size());
        verify(orderRepository).findByClientIdAndOrderStatusIn(eq(1L), anyList());
    }

    @Test
    void updateOrderStatus_Success() {
        // Arrange
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(orderEntityMapper.toOrder(any(OrderEntity.class))).thenReturn(order);

        // Act
        Order result = orderJpaAdapter.updateOrderStatus(1L, OrderStatus.READY);

        // Assert
        assertNotNull(result);
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(OrderEntity.class));
    }

    @Test
    void updateOrderStatus_NotFound() {
        // Arrange
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoDataFoundException.class, () -> orderJpaAdapter.updateOrderStatus(1L, OrderStatus.READY));
        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    void findByStatusAndRestaurantId_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderEntity> page = new PageImpl<>(orderEntities);
        when(orderRepository.findByOrderStatusAndRestaurantId(any(OrderStatus.class), anyLong(), any(Pageable.class)))
                .thenReturn(page);
        when(orderEntityMapper.toOrderList(anyList())).thenReturn(orders);

        // Act
        List<Order> result = orderJpaAdapter.findByStatusAndRestaurantId(OrderStatus.PENDING, 1L, pageable);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(orders.size(), result.size());
        verify(orderRepository).findByOrderStatusAndRestaurantId(eq(OrderStatus.PENDING), eq(1L), eq(pageable));
    }

    @Test
    void assignEmployeeToOrder_Success() {
        // Arrange
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        doNothing().when(orderRepository).assignEmployeeToOrder(anyLong(), anyLong());
        when(orderEntityMapper.toOrder(any(OrderEntity.class))).thenReturn(order);

        // Act
        Order result = orderJpaAdapter.assignEmployeeToOrder(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(orderRepository).assignEmployeeToOrder(1L, 1L);
        verify(orderRepository).findById(1L);
    }
}