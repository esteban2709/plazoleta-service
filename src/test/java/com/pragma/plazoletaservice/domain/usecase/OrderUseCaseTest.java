package com.pragma.plazoletaservice.domain.usecase;

import com.pragma.plazoletaservice.application.dto.response.EmployeeAverageTimeDto;
import com.pragma.plazoletaservice.domain.exception.CustomException;
import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.domain.model.*;
import com.pragma.plazoletaservice.domain.spi.*;
import com.pragma.plazoletaservice.infraestructure.out.clients.dto.TraceabilityLogDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IOrderDishPersistencePort orderDishPersistencePort;

    @Mock
    private IUserClientPort userClientPort;

    @Mock
    private ITokenUtilsPort tokenUtilsPort;

    @Mock
    private IMessagingClientPort messagingClientPort;

    @Mock
    private ITraceabilityLogClientPort traceabilityLogClientPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order order;
    private User client;
    private User chef;
    private Restaurant restaurant;
    private List<OrderDish> orderDishList;
    private TraceabilityLog traceabilityLog;

    @BeforeEach
    void setUp() {
        // Inicializar cliente
        client = new User();
        client.setId(1L);
        client.setEmail("cliente@example.com");
        client.setPhoneNumber("+573001234567");

        // Inicializar chef
        chef = new User();
        chef.setId(2L);
        chef.setEmail("chef@example.com");

        // Inicializar restaurante
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Test");

        // Inicializar OrderDish
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Plato Test");
        dish.setPrice(15000L);

        OrderDish orderDish = new OrderDish();
        orderDish.setId(1L);
        orderDish.setDish(dish);
        orderDish.setQuantity(2);

        orderDishList = new ArrayList<>();
        orderDishList.add(orderDish);

        // Inicializar Order
        order = new Order();
        order.setId(1L);
        order.setClient(client);
        order.setChef(chef);
        order.setRestaurant(restaurant);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDishList(orderDishList);

        // Inicializar TraceabilityLog
        traceabilityLog = new TraceabilityLog();
        traceabilityLog.setId("1L");
        traceabilityLog.setIdOrder(1L);
        traceabilityLog.setIdClient(1L);
        traceabilityLog.setIdEmployee(2L);
        traceabilityLog.setDate(Instant.now());
        traceabilityLog.setNewState(OrderStatus.PREPARING.toString());
        traceabilityLog.setPreviousState(OrderStatus.PENDING.toString());
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(orderPersistencePort.findAllOrdersByUserId(anyLong())).thenReturn(Collections.emptyList());
        when(orderPersistencePort.createOrder(any(Order.class))).thenReturn(order);
        when(orderDishPersistencePort.saverOrderDish(anyList())).thenReturn(orderDishList);
        when(userClientPort.getUserById(anyLong())).thenReturn(client);
        when(traceabilityLogClientPort.saveLog(any(TraceabilityLogDto.class))).thenReturn(traceabilityLog);

        // Act
        Order result = orderUseCase.createOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(OrderStatus.PENDING, result.getOrderStatus());
        assertEquals(client.getId(), result.getClient().getId());
        assertEquals(chef.getId(), result.getChef().getId());
        assertEquals(restaurant.getId(), result.getRestaurant().getId());
        assertNotNull(result.getOrderDishList());
        assertEquals(1, result.getOrderDishList().size());

        verify(orderPersistencePort).findAllOrdersByUserId(anyLong());
        verify(orderPersistencePort).createOrder(any(Order.class));
        verify(orderDishPersistencePort).saverOrderDish(anyList());
        verify(userClientPort).getUserById(anyLong());
        verify(traceabilityLogClientPort).saveLog(any(TraceabilityLogDto.class));
    }

    @Test
    void createOrder_ThrowsExceptionWhenClientHasActiveOrders() {
        // Arrange
        when(orderPersistencePort.findAllOrdersByUserId(anyLong())).thenReturn(Arrays.asList(order));

        // Act & Assert
        assertThrows(CustomException.class, () -> orderUseCase.createOrder(order));
        verify(orderPersistencePort).findAllOrdersByUserId(anyLong());
        verifyNoMoreInteractions(orderPersistencePort, orderDishPersistencePort, userClientPort, messagingClientPort, traceabilityLogClientPort);
    }

    @Test
    void findOrderById_Success() {
        // Arrange
        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);

        // Act
        Order result = orderUseCase.findOrderById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(OrderStatus.PENDING, result.getOrderStatus());
        verify(orderPersistencePort).findOrderById(1L);
    }

    @Test
    void updateOrder_Success() {
        // Arrange
        when(orderPersistencePort.updateOrder(any(Order.class))).thenReturn(order);

        // Act
        Order result = orderUseCase.updateOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(OrderStatus.PENDING, result.getOrderStatus());
        verify(orderPersistencePort).updateOrder(order);
    }

    @Test
    void findAllOrdersByUserId_Success() {
        // Arrange
        List<Order> orderList = Arrays.asList(order);
        when(orderPersistencePort.findAllOrdersByUserId(anyLong())).thenReturn(orderList);

        // Act
        List<Order> result = orderUseCase.findAllOrdersByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(orderPersistencePort).findAllOrdersByUserId(1L);
    }

    @Test
    void updateOrderStatus_ToReady_SendsSecurityCode() {
        // Arrange
        order.setOrderStatus(OrderStatus.PREPARING);
        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);
        when(orderPersistencePort.updateOrderStatus(anyLong(), any(OrderStatus.class))).thenReturn(order);
        when(orderDishPersistencePort.getAllOrdersByDish(anyLong())).thenReturn(orderDishList);
        when(userClientPort.getUserById(eq(1L))).thenReturn(client);
        when(userClientPort.getUserById(eq(2L))).thenReturn(chef);
        when(orderPersistencePort.updateOrder(any(Order.class))).thenReturn(order);
        doNothing().when(messagingClientPort).sendMessage(any(Message.class));
        when(traceabilityLogClientPort.saveLog(any(TraceabilityLogDto.class))).thenReturn(traceabilityLog);


        // Act
        Order result = orderUseCase.updateOrderStatus(1L, OrderStatus.READY, null);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.PREPARING, result.getOrderStatus());
        assertNotNull(result.getOrderDishList());

        verify(orderPersistencePort).findOrderById(1L);
        verify(orderPersistencePort).updateOrderStatus(1L, OrderStatus.READY);
        verify(orderDishPersistencePort).getAllOrdersByDish(1L);
        verify(userClientPort).getUserById(1L);
        verify(userClientPort).getUserById(2L);
        verify(orderPersistencePort).updateOrder(any(Order.class));
        verify(messagingClientPort).sendMessage(any(Message.class));
        verify(traceabilityLogClientPort).saveLog(any(TraceabilityLogDto.class));
    }

    @Test
    void updateOrderStatus_ToDelivered_ValidatesSecurityCode() {
        // Arrange
        order.setOrderStatus(OrderStatus.READY);
        order.setSecurityCode("1234");

        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);
        when(orderPersistencePort.updateOrderStatus(anyLong(), any(OrderStatus.class))).thenReturn(order);
        when(orderDishPersistencePort.getAllOrdersByDish(anyLong())).thenReturn(orderDishList);
        when(userClientPort.getUserById(eq(1L))).thenReturn(client);
        when(userClientPort.getUserById(eq(2L))).thenReturn(chef);
        when(traceabilityLogClientPort.saveLog(any(TraceabilityLogDto.class))).thenReturn(traceabilityLog);

        // Act
        Order result = orderUseCase.updateOrderStatus(1L, OrderStatus.DELIVERED, "1234");

        // Assert
        assertNotNull(result);
        verify(traceabilityLogClientPort).saveLog(any(TraceabilityLogDto.class));
    }

    @Test
    void updateOrderStatus_FromPendingToCancelled_Success() {
        // Arrange
        order.setOrderStatus(OrderStatus.PENDING);

        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);
        when(orderPersistencePort.updateOrderStatus(anyLong(), any(OrderStatus.class))).thenReturn(order);
        when(orderDishPersistencePort.getAllOrdersByDish(anyLong())).thenReturn(orderDishList);
        when(userClientPort.getUserById(eq(1L))).thenReturn(client);
        when(userClientPort.getUserById(eq(2L))).thenReturn(chef);
        when(traceabilityLogClientPort.saveLog(any(TraceabilityLogDto.class))).thenReturn(traceabilityLog);

        // Act
        Order result = orderUseCase.updateOrderStatus(1L, OrderStatus.CANCELLED, null);

        // Assert
        assertNotNull(result);
        verify(orderPersistencePort).updateOrderStatus(1L, OrderStatus.CANCELLED);
        verify(traceabilityLogClientPort).saveLog(any(TraceabilityLogDto.class));
    }

    @Test
    void updateOrderStatus_FromPreparingToCancelled_ThrowsException() {
        // Arrange
        order.setOrderStatus(OrderStatus.PREPARING);

        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);

        // Act & Assert
        assertThrows(CustomException.class, () ->
                orderUseCase.updateOrderStatus(1L, OrderStatus.CANCELLED, null));

        verify(orderPersistencePort).findOrderById(1L);
        verifyNoMoreInteractions(orderPersistencePort, orderDishPersistencePort, messagingClientPort, traceabilityLogClientPort);
    }

    @Test
    void updateOrderStatus_FromDeliveringToAnyOtherState_ThrowsException() {
        // Arrange
        order.setOrderStatus(OrderStatus.DELIVERED);

        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);

        // Act & Assert
        assertThrows(CustomException.class, () ->
                orderUseCase.updateOrderStatus(1L, OrderStatus.PREPARING, null));

        verify(orderPersistencePort).findOrderById(1L);
        verifyNoMoreInteractions(orderPersistencePort, orderDishPersistencePort, messagingClientPort, traceabilityLogClientPort);
    }

    @Test
    void updateOrderStatus_FromDeliveredToReady_Success() {
        // Arrange
        order.setOrderStatus(OrderStatus.DELIVERED);

        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);
        when(orderPersistencePort.updateOrderStatus(anyLong(), any(OrderStatus.class))).thenReturn(order);
        when(orderDishPersistencePort.getAllOrdersByDish(anyLong())).thenReturn(orderDishList);
        when(userClientPort.getUserById(eq(1L))).thenReturn(client);
        when(userClientPort.getUserById(eq(2L))).thenReturn(chef);
        when(traceabilityLogClientPort.saveLog(any(TraceabilityLogDto.class))).thenReturn(traceabilityLog);

        // Act
        Order result = orderUseCase.updateOrderStatus(1L, OrderStatus.READY, null);

        // Assert
        assertNotNull(result);
        verify(orderPersistencePort).updateOrderStatus(1L, OrderStatus.READY);
        verify(traceabilityLogClientPort).saveLog(any(TraceabilityLogDto.class));
    }

    @Test
    void updateOrderStatus_FromPendingToDelivered_ThrowsException() {
        // Arrange
        order.setOrderStatus(OrderStatus.PENDING);

        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);

        // Act & Assert
        assertThrows(CustomException.class, () ->
                orderUseCase.updateOrderStatus(1L, OrderStatus.DELIVERED, "1234"));

        verify(orderPersistencePort).findOrderById(1L);
        verifyNoMoreInteractions(orderPersistencePort, orderDishPersistencePort, messagingClientPort, traceabilityLogClientPort);
    }

    @Test
    void updateOrderStatus_FromPreparingToDelivered_ThrowsException() {
        // Arrange
        order.setOrderStatus(OrderStatus.PREPARING);

        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);

        // Act & Assert
        assertThrows(CustomException.class, () ->
                orderUseCase.updateOrderStatus(1L, OrderStatus.DELIVERED, "1234"));

        verify(orderPersistencePort).findOrderById(1L);
        verifyNoMoreInteractions(orderPersistencePort, orderDishPersistencePort, messagingClientPort, traceabilityLogClientPort);
    }

    @Test
    void updateOrderStatus_FromReadyToDelivered_MissingSecurityCode_ThrowsException() {
        // Arrange
        order.setOrderStatus(OrderStatus.READY);
        order.setSecurityCode("1234");

        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);

        // Act & Assert
        assertThrows(CustomException.class, () ->
                orderUseCase.updateOrderStatus(1L, OrderStatus.DELIVERED, null));

        verify(orderPersistencePort).findOrderById(1L);
        verifyNoMoreInteractions(orderPersistencePort, orderDishPersistencePort, messagingClientPort, traceabilityLogClientPort);
    }

    @Test
    void updateOrderStatus_ToDelivered_InvalidSecurityCode_ThrowsException() {
        // Arrange
        order.setOrderStatus(OrderStatus.READY);
        order.setSecurityCode("1234");

        when(orderPersistencePort.findOrderById(anyLong())).thenReturn(order);

        // Act & Assert
        assertThrows(CustomException.class, () ->
                orderUseCase.updateOrderStatus(1L, OrderStatus.DELIVERED, "4321"));

        verify(orderPersistencePort).findOrderById(1L);
        verifyNoMoreInteractions(orderPersistencePort, userClientPort, orderDishPersistencePort, messagingClientPort, traceabilityLogClientPort);
    }

    @Test
    void getOrdersLogsHistory_Success() {
        // Arrange
        Long orderId = 1L;
        Long clientId = 1L;
        List<TraceabilityLog> logs = Arrays.asList(traceabilityLog);

        when(tokenUtilsPort.getUserId()).thenReturn(clientId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(order);
        when(traceabilityLogClientPort.getLogsByOrderId(orderId)).thenReturn(logs);

        // Act
        List<TraceabilityLog> result = orderUseCase.getOrdersLogsHistory(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderId, result.get(0).getIdOrder());

        verify(tokenUtilsPort).getUserId();
        verify(orderPersistencePort).findOrderById(orderId);
        verify(traceabilityLogClientPort).getLogsByOrderId(orderId);
    }

    @Test
    void getOrdersLogsHistory_ThrowsExceptionWhenNotOrderOwner() {
        // Arrange
        Long orderId = 1L;
        Long differentClientId = 2L;

        when(tokenUtilsPort.getUserId()).thenReturn(differentClientId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(order);

        // Act & Assert
        assertThrows(CustomException.class, () -> orderUseCase.getOrdersLogsHistory(orderId));

        verify(tokenUtilsPort).getUserId();
        verify(orderPersistencePort).findOrderById(orderId);
        verifyNoInteractions(traceabilityLogClientPort);
    }

    @Test
    void getEmployeeOrderAverageDurations() {
        // Arrange
        List<EmployeeAverageTimeDto> averageTimes = new ArrayList<>();
        EmployeeAverageTimeDto dto = new EmployeeAverageTimeDto();
        dto.setIdEmployee(2L);
        dto.setAverageTimeMilliseconds(15.5);
        averageTimes.add(dto);

        when(traceabilityLogClientPort.getEmployeeOrderAverageDurations()).thenReturn(averageTimes);

        // Act
        List<EmployeeAverageTimeDto> result = orderUseCase.getEmployeeOrderAverageDurations();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getIdEmployee());
        assertEquals(15.5, result.get(0).getAverageTimeMilliseconds());

        verify(traceabilityLogClientPort).getEmployeeOrderAverageDurations();
    }

    @Test
    void setOrderDishes() {
        // Arrange
        Order orderWithoutDishes = new Order();
        orderWithoutDishes.setId(1L);
        when(orderDishPersistencePort.getAllOrdersByDish(anyLong())).thenReturn(orderDishList);

        // Act
        Order result = orderUseCase.setOrderDishes(orderWithoutDishes);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(orderDishList, result.getOrderDishList());
        verify(orderDishPersistencePort).getAllOrdersByDish(1L);
    }

    @Test
    void assignEmployeeToOrder_Success() {
        // Arrange
        Order assignedOrder = new Order();
        User chef = new User();
        chef.setId(2L);
        chef.setName("Chef");

        assignedOrder.setId(1L);
        assignedOrder.setOrderStatus(OrderStatus.PENDING);
        assignedOrder.setChef(chef);

        when(orderPersistencePort.assignEmployeeToOrder(anyLong(), anyLong())).thenReturn(assignedOrder);
        when(orderDishPersistencePort.getAllOrdersByDish(anyLong())).thenReturn(orderDishList);

        // Act
        Order result = orderUseCase.assignEmployeeToOrder(2L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(OrderStatus.PENDING, result.getOrderStatus());
        assertEquals(2L, result.getChef().getId());
        assertEquals(orderDishList, result.getOrderDishList());

        verify(orderPersistencePort).assignEmployeeToOrder(2L, 1L);
        verify(orderDishPersistencePort).getAllOrdersByDish(1L);
    }

    @Test
    void getOrdersByState_Success() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        Long userId = 2L;
        Long restaurantId = 3L;
        User employee = new User();
        employee.setId(userId);
        employee.setRestaurantId(restaurantId);
        List<Order> orders = Arrays.asList(order);

        when(tokenUtilsPort.getUserId()).thenReturn(userId);
        when(userClientPort.getUserById(userId)).thenReturn(employee);
        when(orderPersistencePort.findByStatusAndRestaurantId(OrderStatus.PENDING, restaurantId, pageable)).thenReturn(orders);
        when(orderDishPersistencePort.getAllOrdersByDish(anyLong())).thenReturn(orderDishList);

        // Act
        List<Order> result = orderUseCase.getOrdersByState(OrderStatus.PENDING, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(orderDishList, result.get(0).getOrderDishList());

        verify(tokenUtilsPort).getUserId();
        verify(userClientPort).getUserById(userId);
        verify(orderPersistencePort).findByStatusAndRestaurantId(OrderStatus.PENDING, restaurantId, pageable);
        verify(orderDishPersistencePort).getAllOrdersByDish(1L);
    }

}