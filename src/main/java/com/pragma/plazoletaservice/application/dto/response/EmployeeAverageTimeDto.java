package com.pragma.plazoletaservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAverageTimeDto {
    private Long idEmployee;
    private Double averageTimeMilliseconds;
}
