package com.pragma.plazoletaservice.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestaurantResponseDto {
    private Long id;
    private String name;
    private Long nit;
    private String address;
    private String phone;
    private String urlLogo;
    private Long ownerId;
}
