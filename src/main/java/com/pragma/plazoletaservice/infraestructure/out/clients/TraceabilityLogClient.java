package com.pragma.plazoletaservice.infraestructure.out.clients;

import com.pragma.plazoletaservice.application.dto.response.EmployeeAverageTimeDto;
import com.pragma.plazoletaservice.domain.model.TraceabilityLog;
import com.pragma.plazoletaservice.infraestructure.out.clients.dto.TraceabilityLogDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "traceability-service", url = "localhost:8084/api/v1/traceability")
public interface TraceabilityLogClient {

    @PostMapping("/logs")
    TraceabilityLog saveLogs(@RequestBody TraceabilityLogDto traceabilityLogDto);

    @GetMapping("/logs/{orderId}")
    List<TraceabilityLog> getLogsByOrderId(@PathVariable Long orderId);

    @GetMapping("/logs/employee-average-durations")
    List<EmployeeAverageTimeDto> getEmployeeOrderAverageDurations();
}
