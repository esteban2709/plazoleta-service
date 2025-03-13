package com.pragma.plazoletaservice.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TraceabilityLogResponseDto {
    private String id;
    private Long idOrder;
    private Long idClient;
    private String emailClient;
    private Instant date;
    private String previousState;
    private String newState;
    private Long idEmployee;
    private String emailEmployee;
}
