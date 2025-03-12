package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.domain.model.OrderDish;

import java.util.List;

public interface IOrderDishPersistencePort {

    List<OrderDish> saverOrderDish(List<OrderDish> orderDishes);

    List<OrderDish> getAllOrdersByDish(Long id);
}
