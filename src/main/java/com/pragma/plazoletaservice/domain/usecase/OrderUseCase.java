package com.pragma.plazoletaservice.domain.usecase;

import com.pragma.plazoletaservice.domain.api.IOrderServicePort;
import com.pragma.plazoletaservice.domain.exception.CustomException;
import com.pragma.plazoletaservice.domain.exception.ExceptionMessage;
import com.pragma.plazoletaservice.domain.helpers.Constants;
import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.domain.model.Message;
import com.pragma.plazoletaservice.domain.model.Order;
import com.pragma.plazoletaservice.domain.model.OrderDish;
import com.pragma.plazoletaservice.domain.model.User;
import com.pragma.plazoletaservice.domain.spi.*;
import com.pragma.plazoletaservice.infraestructure.exception.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IUserClientPort userClientPort;
    private final ITokenUtilsPort tokenUtilsPort;
    private final IMessagingClientPort messagingClientPort;


    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort,
                        IUserClientPort userClientPort, ITokenUtilsPort tokenUtilsPort, IMessagingClientPort messagingClientPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.userClientPort = userClientPort;
        this.tokenUtilsPort = tokenUtilsPort;
        this.messagingClientPort = messagingClientPort;
    }

    @Override
    public Order createOrder(Order order) {
        List<Order> ordersInProgress = orderPersistencePort.findAllOrdersByUserId(order.getClient().getId());

        if (!ordersInProgress.isEmpty()) {
            throw new CustomException(ExceptionMessage.CLIENT_HAS_ACTIVE_ORDER.getMessage());
        }
        Order orderCreated = orderPersistencePort.createOrder(order);
        List<OrderDish> orderDishList = order.getOrderDishList();
        orderDishList.forEach(orderDish -> orderDish.setOrder(orderCreated));
        orderCreated.setOrderDishList(orderDishPersistencePort.saverOrderDish(orderDishList));
        return orderCreated;
    }

    @Override
    public Order findOrderById(Long id) {
        return orderPersistencePort.findOrderById(id);
    }

    @Override
    public Order updateOrder(Order order) {
        return orderPersistencePort.updateOrder(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderPersistencePort.deleteOrder(id);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderPersistencePort.findAllOrders();
    }

    @Override
    public List<Order> findAllOrdersByUserId(Long userId) {
        return orderPersistencePort.findAllOrdersByUserId(userId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status, String securityCode) {
        // Obtener el pedido actual
        Order currentOrder = orderPersistencePort.findOrderById(orderId);

        // Validar cambios de estado según las reglas
        isInvalidStateTransition(status, securityCode, currentOrder);

        // Actualizar el estado
        Order order = setOrderDishes(orderPersistencePort.updateOrderStatus(orderId, status));

        if (status == OrderStatus.READY) {
            // Generar código de seguridad para pedidos listos
            sendSecurityCode(order);
        }

        return order;
    }

    private static void isInvalidStateTransition(OrderStatus status, String securityCode, Order currentOrder) {
        if (status == OrderStatus.DELIVERED) {
            // Regla 1: Solo los pedidos en estado "Listo" pueden pasar a "Entregado"
            if (currentOrder.getOrderStatus() != OrderStatus.READY) {
                throw new CustomException(ExceptionMessage.INVALID_STATE_TRANSITION.getMessage());
            }

            // Regla 3: Validar el código de seguridad
            if (securityCode == null || !securityCode.equals(currentOrder.getSecurityCode())) {
                throw new CustomException(ExceptionMessage.INVALID_SECURITY_CODE.getMessage());
            }
        } else if (currentOrder.getOrderStatus() == OrderStatus.DELIVERED && status != OrderStatus.READY) {
            // Regla 2: Ningún pedido "Entregado" puede cambiar a otro estado diferente de "Listo"
            throw new CustomException(ExceptionMessage.DELIVERED_ORDER_CANNOT_BE_MODIFIED.getMessage());
        } else if (status == OrderStatus.CANCELLED) {
            // Validar que solo los pedidos en estado "Pendiente" pueden ser cancelados
            if (currentOrder.getOrderStatus() != OrderStatus.PENDING) {
                throw new CustomException(ExceptionMessage.ORDER_IS_PREPARING.getMessage());
            }
        }
    }

    private void sendSecurityCode(Order order) {
        User user = userClientPort.getUserById(order.getClient().getId());
        String randomCode = generateSecurityCode();
        order.setSecurityCode(randomCode);
        orderPersistencePort.updateOrder(order);
        messagingClientPort.sendMessage(new Message(
                user.getPhoneNumber(),
                Constants.ORDER_READY_MESSAGE + randomCode
        ));
    }

    private String generateSecurityCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
    }

    @Override
    public List<Order> getOrdersByState(OrderStatus status, Pageable pageable) {
        Long currentUserId = tokenUtilsPort.getUserId();
        User user = userClientPort.getUserById(currentUserId);
        Long restaurantId = user.getRestaurantId();
        if (restaurantId == null) {
            throw new NoDataFoundException();
        }
        List<Order> orders = orderPersistencePort.findByStatusAndRestaurantId(status, restaurantId, pageable);
        orders.forEach(order -> {
            List<OrderDish> orderDishes = orderDishPersistencePort.getAllOrdersByDish(order.getId());
            order.setOrderDishList(orderDishes);
        });
        return orders;
    }

    @Override
    public Order assignEmployeeToOrder(Long id, Long orderId) {
        return setOrderDishes(orderPersistencePort.assignEmployeeToOrder(id, orderId));
    }

    @Override
    public Order setOrderDishes(Order order) {
        order.setOrderDishList(orderDishPersistencePort.getAllOrdersByDish(order.getId()));
        return order;
    }
}
