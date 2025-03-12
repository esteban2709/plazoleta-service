package com.pragma.plazoletaservice.infraestructure.out.clients.mapper;

import com.pragma.plazoletaservice.domain.model.Message;
import com.pragma.plazoletaservice.infraestructure.out.clients.dto.MessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IMessageDtoMapper {

    Message toMessage(MessageDto messageDto);

    MessageDto toMessageDto(Message message);
}
