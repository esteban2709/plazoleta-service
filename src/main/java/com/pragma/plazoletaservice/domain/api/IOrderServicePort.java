package com.pragma.plazoletaservice.domain.api;

import com.pragma.plazoletaservice.application.dto.response.EmployeeAverageTimeDto;
import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.domain.model.Order;
import com.pragma.plazoletaservice.domain.model.TraceabilityLog;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderServicePort {
    Order createOrder(Order order);

    Order findOrderById(Long id);

    Order updateOrder(Order order);

    void deleteOrder(Long id);

    List<Order> findAllOrders();

    List<Order> findAllOrdersByUserId(Long userId);

    List<Order> getOrdersByState(OrderStatus status, Pageable pageable);

    Order assignEmployeeToOrder(Long id, Long orderId);

    Order setOrderDishes(Order order);

    Order updateOrderStatus(Long orderId, OrderStatus status, String securityCode);

    List<TraceabilityLog> getOrdersLogsHistory(Long id);

    List<EmployeeAverageTimeDto> getEmployeeOrderAverageDurations();
}
