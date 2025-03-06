package com.pragma.plazoletaservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;
    private String description;
}