package com.pragma.plazoletaservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraceabilityLog {
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
