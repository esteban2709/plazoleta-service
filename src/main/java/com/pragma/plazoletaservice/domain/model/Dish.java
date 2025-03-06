package com.pragma.plazoletaservice.domain.model;

import lombok.Data;

@Data
public class Dish {

    private Long id;
    private String name;
    private Category category;
    private String description;
    private Long price;
    private String urlImage;
    private Boolean active;
    private Restaurant restaurant;
}
