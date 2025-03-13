package com.pragma.plazoletaservice.application.handler.impl;

import com.pragma.plazoletaservice.application.dto.request.OrderRequestDto;
import com.pragma.plazoletaservice.application.dto.response.EmployeeAverageTimeDto;
import com.pragma.plazoletaservice.application.dto.response.OrderResponseDto;
import com.pragma.plazoletaservice.application.dto.response.TraceabilityLogResponseDto;
import com.pragma.plazoletaservice.application.handler.IOrderHandler;
import com.pragma.plazoletaservice.application.mapper.IOrderDishRequestMapper;
import com.pragma.plazoletaservice.application.mapper.IOrderRequestMapper;
import com.pragma.plazoletaservice.application.mapper.IOrderResponseMapper;
import com.pragma.plazoletaservice.domain.api.IOrderServicePort;
import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.domain.model.Order;
import com.pragma.plazoletaservice.domain.model.OrderDish;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;
    private final IOrderDishRequestMapper orderDishRequestMapper;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = orderRequestMapper.toOrder(orderRequestDto);
        List<OrderDish> orderDishList = new ArrayList<>();
        orderRequestDto.getOrderDishList().forEach(orderDishRequestDto -> {
            orderDishList.add(orderDishRequestMapper.toOrderDish(orderDishRequestDto));
        });
        order.setOrderDishList(orderDishList);
        return orderResponseMapper.toResponse(orderServicePort.createOrder(order));
    }

    @Override
    public OrderResponseDto findOrderById(Long id) {
        return orderResponseMapper.toResponse(orderServicePort.findOrderById(id));
    }

    @Override
    public OrderResponseDto updateOrder(Long id, OrderRequestDto orderRequestDto) {
        Order order = orderRequestMapper.toOrder(orderRequestDto);
        return orderResponseMapper.toResponse(orderServicePort.updateOrder(order));
    }

    @Override
    public void deleteOrder(Long id) {
        orderServicePort.deleteOrder(id);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, OrderStatus status, String securityCode) {
        return orderResponseMapper.toResponse(orderServicePort.updateOrderStatus(id, status, securityCode));
    }

    @Override
    public List<OrderResponseDto> findAllOrders() {
        return orderResponseMapper.toResponseList(orderServicePort.findAllOrders());
    }

    @Override
    public List<OrderResponseDto> findAllOrdersByUserId(Long userId) {
        return orderResponseMapper.toResponseList(orderServicePort.findAllOrdersByUserId(userId));
    }

    @Override
    public List<OrderResponseDto> getOrdersByState(OrderStatus status, Pageable pageable) {
        return orderResponseMapper.toResponseList(orderServicePort.getOrdersByState(status, pageable));

    }

    @Override
    public OrderResponseDto assignEmployeeToOrder(Long id, Long orderId) {
        return orderResponseMapper.toResponse(orderServicePort.assignEmployeeToOrder(id, orderId));
    }

    @Override
    public List<TraceabilityLogResponseDto> getOrdersLogsHistory(Long id) {
        return orderResponseMapper.toTraceabilityLogDtoList(orderServicePort.getOrdersLogsHistory(id));
    }

    @Override
    public List<EmployeeAverageTimeDto> getEmployeeOrderAverageDurations() {
        return orderServicePort.getEmployeeOrderAverageDurations();
    }
}
