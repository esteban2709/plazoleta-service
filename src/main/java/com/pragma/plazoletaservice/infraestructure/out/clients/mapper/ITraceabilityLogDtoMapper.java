package com.pragma.plazoletaservice.infraestructure.out.clients.mapper;

import com.pragma.plazoletaservice.domain.model.TraceabilityLog;
import com.pragma.plazoletaservice.infraestructure.out.clients.dto.TraceabilityLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ITraceabilityLogDtoMapper {

     TraceabilityLogDto toTraceabilityLogDto(TraceabilityLog traceabilityLog);
     TraceabilityLog toTraceabilityLog(TraceabilityLogDto traceabilityLogDto);
}
