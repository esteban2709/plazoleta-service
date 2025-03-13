package com.pragma.plazoletaservice.infraestructure.out.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TraceabilityLogDto {
    private Long idOrder;
    private Long idClient;
    private String emailClient;
    private String previousState;
    private String newState;
    private Long idEmployee;
    private String emailEmployee;
}
