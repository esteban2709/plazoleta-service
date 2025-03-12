package com.pragma.plazoletaservice.infraestructure.out.jpa.repository;

import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {


    List<OrderEntity> findByClientIdAndOrderStatusIn(Long clientId, List<String> statuses);

    Page<OrderEntity> findByOrderStatusAndRestaurantId(OrderStatus status, Long restaurantId, Pageable pageable);

    @Modifying
    @Query("UPDATE OrderEntity o SET o.chefId = :id WHERE o.id = :orderId")
    void assignEmployeeToOrder(Long id, Long orderId);

}
