package com.pragma.plazoletaservice.infraestructure.out.jpa.repository;

import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
