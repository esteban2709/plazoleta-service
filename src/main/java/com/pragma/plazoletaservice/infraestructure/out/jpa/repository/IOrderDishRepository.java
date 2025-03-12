package com.pragma.plazoletaservice.infraestructure.out.jpa.repository;

import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.OrderDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderDishRepository extends JpaRepository<OrderDishEntity, Long> {
    List<OrderDishEntity> getOrderDishEntitiesByOrderId(Long id);
}
