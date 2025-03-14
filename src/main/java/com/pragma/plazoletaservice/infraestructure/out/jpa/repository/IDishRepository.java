package com.pragma.plazoletaservice.infraestructure.out.jpa.repository;

import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    Optional<DishEntity> findById(Long id);

    Page<DishEntity> findAllByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId, Pageable pageable);

    Page<DishEntity> findAllByRestaurantId(Long restaurantId, Pageable pageable);

}
