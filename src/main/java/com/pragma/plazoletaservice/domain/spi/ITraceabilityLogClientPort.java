package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.application.dto.response.EmployeeAverageTimeDto;
import com.pragma.plazoletaservice.domain.model.TraceabilityLog;
import com.pragma.plazoletaservice.infraestructure.out.clients.dto.TraceabilityLogDto;

import java.util.List;

public interface ITraceabilityLogClientPort {

    TraceabilityLog saveLog(TraceabilityLogDto log);
    List<TraceabilityLog> getLogsByOrderId(Long orderId);

    List<EmployeeAverageTimeDto> getEmployeeOrderAverageDurations();
}
