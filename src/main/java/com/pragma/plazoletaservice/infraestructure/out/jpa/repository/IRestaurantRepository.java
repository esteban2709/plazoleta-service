package com.pragma.plazoletaservice.infraestructure.out.jpa.repository;

import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findById(Long id);

    Page<RestaurantEntity> findAll(Pageable pageable);
}
