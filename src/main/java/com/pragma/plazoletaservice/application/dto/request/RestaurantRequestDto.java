package com.pragma.plazoletaservice.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRequestDto {
    private String name;
    private Long nit;
    private String address;
    private String phone;
    private String urlLogo;
    private Long ownerId;
}
