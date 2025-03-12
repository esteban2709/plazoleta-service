package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.domain.model.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderPersistencePort {

    Order createOrder(Order order);

    Order findOrderById(Long id);

    Order updateOrder(Order order);

    void deleteOrder(Long id);

    List<Order> findAllOrders();

    List<Order> findAllOrdersByUserId(Long userId);

    Order updateOrderStatus(Long orderId, OrderStatus status);

    List<Order> findByStatusAndRestaurantId(OrderStatus status, Long restaurantId, Pageable pageable);

    Order assignEmployeeToOrder(Long id, Long orderId);
}
