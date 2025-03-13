package com.pragma.plazoletaservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeAverageTimeDto {
    private Long idEmployee;
    private Double averageTimeMilliseconds;
}
