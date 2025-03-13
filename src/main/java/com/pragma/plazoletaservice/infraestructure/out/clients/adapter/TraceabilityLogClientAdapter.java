package com.pragma.plazoletaservice.infraestructure.out.clients.adapter;

import com.pragma.plazoletaservice.application.dto.response.EmployeeAverageTimeDto;
import com.pragma.plazoletaservice.domain.model.TraceabilityLog;
import com.pragma.plazoletaservice.domain.spi.ITraceabilityLogClientPort;
import com.pragma.plazoletaservice.infraestructure.out.clients.TraceabilityLogClient;
import com.pragma.plazoletaservice.infraestructure.out.clients.dto.TraceabilityLogDto;
import com.pragma.plazoletaservice.infraestructure.out.clients.mapper.ITraceabilityLogDtoMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TraceabilityLogClientAdapter implements ITraceabilityLogClientPort {

    private final TraceabilityLogClient traceabilityLogClient;
    private final ITraceabilityLogDtoMapper traceabilityLogDtoMapper;

    @Override
    public TraceabilityLog saveLog(TraceabilityLogDto log) {
        return traceabilityLogClient.saveLogs(log);
    }

    @Override
    public List<TraceabilityLog> getLogsByOrderId(Long orderId) {
        return traceabilityLogClient.getLogsByOrderId(orderId);
    }

    @Override
    public List<EmployeeAverageTimeDto> getEmployeeOrderAverageDurations() {
        return traceabilityLogClient.getEmployeeOrderAverageDurations();
    }
}
