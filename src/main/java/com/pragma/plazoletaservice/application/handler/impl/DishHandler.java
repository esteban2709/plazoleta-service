package com.pragma.plazoletaservice.application.handler.impl;

import com.pragma.plazoletaservice.application.dto.request.DishRequestDto;
import com.pragma.plazoletaservice.application.dto.response.DishResponseDto;
import com.pragma.plazoletaservice.application.handler.IDishHandler;
import com.pragma.plazoletaservice.application.mapper.IDishRequestMapper;
import com.pragma.plazoletaservice.application.mapper.IDishResponseMapper;
import com.pragma.plazoletaservice.domain.api.IDishServicePort;
import com.pragma.plazoletaservice.domain.model.Dish;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishResponseMapper dishResponseMapper;
    private final IDishRequestMapper dishRequestMapper;

    @Override
    public DishResponseDto saveDish(DishRequestDto dishRequestDto) {
        Dish dish = dishRequestMapper.toDish(dishRequestDto);
        return dishResponseMapper.toResponse(dishServicePort.saveDish(dish));
    }

    @Override
    public DishResponseDto findDishById(Long id) {
        return dishResponseMapper.toResponse(dishServicePort.findDishById(id));
    }

    @Override
    public DishResponseDto updateDish(Long id, DishRequestDto dishRequestDto) {
        return dishResponseMapper.toResponse(dishServicePort.updateDish(id,dishRequestMapper.toDish(dishRequestDto)));
    }

    @Override
    public DishResponseDto updateDishStatus(Long dishId, boolean active) {
        return dishResponseMapper.toResponse(dishServicePort.updateDishStatus(dishId, active));
    }

    @Override
    public void deleteDish(Long id) {
    dishServicePort.deleteDish(id);
    }

    @Override
    public List<DishResponseDto> findAllDishes() {
        return dishResponseMapper.toResponseList(dishServicePort.findAllDishes());
    }

    @Override
    public List<DishResponseDto> findAllDishesByRestaurantId(Long restaurantId, Pageable pageable) {
        return dishResponseMapper.toResponseList(dishServicePort.findAllDishesByRestaurantId(restaurantId, null, pageable));
    }

    @Override
    public List<DishResponseDto> findAllDishesByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId, Pageable pageable) {
        return dishResponseMapper.toResponseList(dishServicePort.findAllDishesByRestaurantId(restaurantId, categoryId, pageable));
    }
}
