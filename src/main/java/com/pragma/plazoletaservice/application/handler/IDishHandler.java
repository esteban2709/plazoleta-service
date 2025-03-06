package com.pragma.plazoletaservice.application.handler;

import com.pragma.plazoletaservice.application.dto.request.DishRequestDto;
import com.pragma.plazoletaservice.application.dto.response.DishResponseDto;

import java.util.List;

public interface IDishHandler {

    DishResponseDto saveDish(DishRequestDto dishRequestDto);

    DishResponseDto findDishById(Long id);

    DishResponseDto updateDish(Long id, DishRequestDto dishRequestDto);

    DishResponseDto updateDishStatus(Long dishId, boolean active);

    void deleteDish(Long id);

    List<DishResponseDto> findAllDishes();
}
