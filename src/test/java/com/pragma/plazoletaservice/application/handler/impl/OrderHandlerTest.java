package com.pragma.plazoletaservice.application.handler.impl;

import com.pragma.plazoletaservice.application.dto.request.OrderDishRequestDto;
import com.pragma.plazoletaservice.application.dto.request.OrderRequestDto;
import com.pragma.plazoletaservice.application.dto.response.EmployeeAverageTimeDto;
import com.pragma.plazoletaservice.application.dto.response.OrderResponseDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazoletaservice.application.dto.response.TraceabilityLogResponseDto;
import com.pragma.plazoletaservice.application.mapper.IOrderDishRequestMapper;
import com.pragma.plazoletaservice.application.mapper.IOrderRequestMapper;
import com.pragma.plazoletaservice.application.mapper.IOrderResponseMapper;
import com.pragma.plazoletaservice.domain.api.IOrderServicePort;
import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderHandlerTest {

    @Mock
    private IOrderServicePort orderServicePort;

    @Mock
    private IOrderRequestMapper orderRequestMapper;

    @Mock
    private IOrderResponseMapper orderResponseMapper;

    @Mock
    private IOrderDishRequestMapper orderDishRequestMapper;

    @InjectMocks
    private OrderHandler orderHandler;

    private OrderRequestDto orderRequestDto;
    private OrderDishRequestDto orderDishRequestDto;
    private Order order;
    private OrderDish orderDish;
    private OrderResponseDto orderResponseDto;
    private List<OrderResponseDto> orderResponseDtoList;
    private List<TraceabilityLogResponseDto> traceabilityLogResponseDtoList;
    private List<EmployeeAverageTimeDto> employeeAverageTimeDtoList;

    @BeforeEach
    void setUp() {
        // Inicializar DTOs de petición
        orderDishRequestDto = new OrderDishRequestDto();
        orderDishRequestDto.setDishId(1L);
        orderDishRequestDto.setQuantity(2L);

        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setRestaurantId(1L);
        orderRequestDto.setOrderDishList(List.of(orderDishRequestDto));

        // Inicializar modelos de dominio
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Dish 1");
        dish.setDescription("Description 1");
        orderDish = new OrderDish();
        orderDish.setId(1L);
        orderDish.setDish(dish);
        orderDish.setQuantity(2);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurant 1");
        restaurant.setAddress("Address 1");

        order = new Order();
        order.setId(1L);
        order.setRestaurant(restaurant);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDishList(List.of(orderDish));

        // Inicializar DTOs de respuesta
        RestaurantResponseDto restaurantResponseDto = new RestaurantResponseDto();
        restaurantResponseDto.setId(1L);
        restaurantResponseDto.setName("Restaurant 1");

        orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(1L);
        orderResponseDto.setClientId(1L);
        orderResponseDto.setRestaurant(restaurantResponseDto);
        orderResponseDto.setOrderStatus(OrderStatus.PENDING);

        orderResponseDtoList = List.of(orderResponseDto);

        // Inicializar DTOs de trazabilidad
        TraceabilityLogResponseDto traceabilityLog = new TraceabilityLogResponseDto();
        traceabilityLog.setId("1L");
        traceabilityLog.setIdOrder(1L);
        traceabilityLogResponseDtoList = List.of(traceabilityLog);

        // Inicializar DTOs de tiempo promedio
        EmployeeAverageTimeDto employeeAverageTimeDto = new EmployeeAverageTimeDto();
        employeeAverageTimeDto.setIdEmployee(1L);
        employeeAverageTimeDto.setAverageTimeMilliseconds(15.5);
        employeeAverageTimeDtoList = List.of(employeeAverageTimeDto);
    }

    @Test
    void createOrder() {
        // Arrange
        when(orderRequestMapper.toOrder(orderRequestDto)).thenReturn(order);
        when(orderDishRequestMapper.toOrderDish(any(OrderDishRequestDto.class))).thenReturn(orderDish);
        when(orderServicePort.createOrder(any(Order.class))).thenReturn(order);
        when(orderResponseMapper.toResponse(order)).thenReturn(orderResponseDto);

        // Act
        OrderResponseDto result = orderHandler.createOrder(orderRequestDto);

        // Assert
        assertEquals(orderResponseDto, result);
        verify(orderRequestMapper).toOrder(orderRequestDto);
        verify(orderDishRequestMapper).toOrderDish(orderDishRequestDto);
        verify(orderServicePort).createOrder(any(Order.class));
        verify(orderResponseMapper).toResponse(order);
    }

    @Test
    void findOrderById() {
        // Arrange
        when(orderServicePort.findOrderById(1L)).thenReturn(order);
        when(orderResponseMapper.toResponse(order)).thenReturn(orderResponseDto);

        // Act
        OrderResponseDto result = orderHandler.findOrderById(1L);

        // Assert
        assertEquals(orderResponseDto, result);
        verify(orderServicePort).findOrderById(1L);
        verify(orderResponseMapper).toResponse(order);
    }

    @Test
    void updateOrder() {
        // Arrange
        when(orderRequestMapper.toOrder(orderRequestDto)).thenReturn(order);
        when(orderServicePort.updateOrder(order)).thenReturn(order);
        when(orderResponseMapper.toResponse(order)).thenReturn(orderResponseDto);

        // Act
        OrderResponseDto result = orderHandler.updateOrder(1L, orderRequestDto);

        // Assert
        assertEquals(orderResponseDto, result);
        verify(orderRequestMapper).toOrder(orderRequestDto);
        verify(orderServicePort).updateOrder(order);
        verify(orderResponseMapper).toResponse(order);
    }

    @Test
    void updateOrderStatus() {
        // Arrange
        String securityCode = "12345";
        when(orderServicePort.updateOrderStatus(1L, OrderStatus.PREPARING, securityCode)).thenReturn(order);
        when(orderResponseMapper.toResponse(order)).thenReturn(orderResponseDto);

        // Act
        OrderResponseDto result = orderHandler.updateOrderStatus(1L, OrderStatus.PREPARING, securityCode);

        // Assert
        assertEquals(orderResponseDto, result);
        verify(orderServicePort).updateOrderStatus(1L, OrderStatus.PREPARING, securityCode);
        verify(orderResponseMapper).toResponse(order);
    }

    @Test
    void findAllOrdersByUserId() {
        // Arrange
        List<Order> orderList = List.of(order);
        when(orderServicePort.findAllOrdersByUserId(1L)).thenReturn(orderList);
        when(orderResponseMapper.toResponseList(orderList)).thenReturn(orderResponseDtoList);

        // Act
        List<OrderResponseDto> result = orderHandler.findAllOrdersByUserId(1L);

        // Assert
        assertEquals(orderResponseDtoList, result);
        verify(orderServicePort).findAllOrdersByUserId(1L);
        verify(orderResponseMapper).toResponseList(orderList);
    }

    @Test
    void getOrdersByState() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orderList = List.of(order);
        when(orderServicePort.getOrdersByState(OrderStatus.PENDING, pageable)).thenReturn(orderList);
        when(orderResponseMapper.toResponseList(orderList)).thenReturn(orderResponseDtoList);

        // Act
        List<OrderResponseDto> result = orderHandler.getOrdersByState(OrderStatus.PENDING, pageable);

        // Assert
        assertEquals(orderResponseDtoList, result);
        verify(orderServicePort).getOrdersByState(OrderStatus.PENDING, pageable);
        verify(orderResponseMapper).toResponseList(orderList);
    }

    @Test
    void assignEmployeeToOrder() {
        // Arrange
        when(orderServicePort.assignEmployeeToOrder(1L, 1L)).thenReturn(order);
        when(orderResponseMapper.toResponse(order)).thenReturn(orderResponseDto);

        // Act
        OrderResponseDto result = orderHandler.assignEmployeeToOrder(1L, 1L);

        // Assert
        assertEquals(orderResponseDto, result);
        verify(orderServicePort).assignEmployeeToOrder(1L, 1L);
        verify(orderResponseMapper).toResponse(order);
    }

    @Test
    void getOrdersLogsHistory() {
        // Arrange
        List<TraceabilityLog> logsHistory = new ArrayList<>();
        when(orderServicePort.getOrdersLogsHistory(1L)).thenReturn(logsHistory);
        when(orderResponseMapper.toTraceabilityLogDtoList(logsHistory)).thenReturn(traceabilityLogResponseDtoList);

        // Act
        List<TraceabilityLogResponseDto> result = orderHandler.getOrdersLogsHistory(1L);

        // Assert
        assertEquals(traceabilityLogResponseDtoList, result);
        verify(orderServicePort).getOrdersLogsHistory(1L);
        verify(orderResponseMapper).toTraceabilityLogDtoList(logsHistory);
    }

    @Test
    void getEmployeeOrderAverageDurations() {
        // Arrange
        when(orderServicePort.getEmployeeOrderAverageDurations()).thenReturn(employeeAverageTimeDtoList);

        // Act
        List<EmployeeAverageTimeDto> result = orderHandler.getEmployeeOrderAverageDurations();

        // Assert
        assertSame(employeeAverageTimeDtoList, result);
        verify(orderServicePort).getEmployeeOrderAverageDurations();
    }
}