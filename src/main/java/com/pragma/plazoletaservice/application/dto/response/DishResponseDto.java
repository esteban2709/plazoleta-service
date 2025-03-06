package com.pragma.plazoletaservice.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishResponseDto {

    private Long id;
    private String name;
    private Long categoryId;
    private String description;
    private Long price;
    private String urlImage;
    private Boolean active;
    private Long restaurantId;
}
