package com.pragma.plazoletaservice.infraestructure.out.jpa.adapter;

import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.domain.model.Order;
import com.pragma.plazoletaservice.domain.spi.IOrderPersistencePort;
import com.pragma.plazoletaservice.infraestructure.exception.NoDataFoundException;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.OrderEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.RestaurantEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.IOrderRepository;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IRestaurantRepository restaurantRepository;


    @Override
    public Order createOrder(Order order) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(order);

        // Ensure restaurant is fully loaded
        if (order.getRestaurant() != null && order.getRestaurant().getId() != null) {
            RestaurantEntity restaurantEntity = restaurantRepository.findById(order.getRestaurant().getId())
                    .orElseThrow(NoDataFoundException::new);
            orderEntity.setRestaurant(restaurantEntity);
        }

        OrderEntity savedOrder = orderRepository.save(orderEntity);
        return orderEntityMapper.toOrder(savedOrder);
    }

    @Override
    public Order findOrderById(Long id) {
        return orderEntityMapper.toOrder(orderRepository.findById(id).orElse(null));
    }

    @Override
    public Order updateOrder(Order order) {
        return orderEntityMapper.toOrder(orderRepository.save(orderEntityMapper.toEntity(order)));
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        if (orderEntities.isEmpty()) {
            throw new NoDataFoundException();
        }
        return orderEntityMapper.toOrderList(orderEntities);
    }

    @Override
    public List<Order> findAllOrdersByUserId(Long userId) {
        List<OrderEntity> orderEntities = orderRepository.findByClientIdAndOrderStatusIn(
                userId,
                List.of(OrderStatus.PENDING.toString(),
                        OrderStatus.PREPARING.toString(),
                        OrderStatus.READY.toString())
        );
        return orderEntityMapper.toOrderList(orderEntities);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        if (orderEntity == null) {
            throw new NoDataFoundException();
        }
        orderEntity.setOrderStatus(status);
        return orderEntityMapper.toOrder(orderRepository.save(orderEntity));
    }

    @Override
    public List<Order> findByStatusAndRestaurantId(OrderStatus status, Long restaurantId, Pageable pageable) {
        List<OrderEntity> orderEntities = orderRepository.findByOrderStatusAndRestaurantId(
                status, restaurantId, pageable).getContent();
        return orderEntityMapper.toOrderList(orderEntities);
    }

    @Override
    public Order assignEmployeeToOrder(Long id, Long orderId) {
        orderRepository.assignEmployeeToOrder(id, orderId);
        return orderEntityMapper.toOrder(orderRepository.findById(orderId).orElse(null));
    }
}
