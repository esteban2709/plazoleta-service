package com.pragma.plazoletaservice.infraestructure.out.jpa.adapter;

import com.pragma.plazoletaservice.domain.model.OrderDish;
import com.pragma.plazoletaservice.domain.spi.IOrderDishPersistencePort;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.IOrderDishEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.IOrderDishRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderDishJpaAdapter implements IOrderDishPersistencePort {

    private final IOrderDishRepository repository;
    private final IOrderDishEntityMapper orderDishEntityMapper;

    @Override
    public List<OrderDish> saverOrderDish(List<OrderDish> orderDishes) {
        return orderDishEntityMapper.toOrderDishList(repository.saveAll(orderDishEntityMapper.toOrderDishListEntities(orderDishes)));
    }

    @Override
    public List<OrderDish> getAllOrdersByDish(Long id) {
        return orderDishEntityMapper.toOrderDishList(repository.getOrderDishEntitiesByOrderId(id));
    }
}
