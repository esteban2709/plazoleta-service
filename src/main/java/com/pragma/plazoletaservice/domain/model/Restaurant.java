package com.pragma.plazoletaservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

        private Long id;
        private String name;
        private Long nit;
        private String address;
        private String phone;
        private String urlLogo;
        private Long ownerId;
}


