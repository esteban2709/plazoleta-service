package com.pragma.plazoletaservice.application.handler;

import com.pragma.plazoletaservice.application.dto.request.OrderRequestDto;
import com.pragma.plazoletaservice.application.dto.response.EmployeeAverageTimeDto;
import com.pragma.plazoletaservice.application.dto.response.OrderResponseDto;
import com.pragma.plazoletaservice.application.dto.response.TraceabilityLogResponseDto;
import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderHandler {

    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto findOrderById(Long id);

    OrderResponseDto updateOrder(Long id, OrderRequestDto orderRequestDto);

    OrderResponseDto updateOrderStatus(Long id, OrderStatus status, String securityCode);

    List<OrderResponseDto> findAllOrdersByUserId(Long userId);

    List<OrderResponseDto> getOrdersByState(OrderStatus status, Pageable pageable);

    OrderResponseDto assignEmployeeToOrder(Long id, Long orderId);

    List<TraceabilityLogResponseDto> getOrdersLogsHistory(Long id);

    List<EmployeeAverageTimeDto> getEmployeeOrderAverageDurations();
}
