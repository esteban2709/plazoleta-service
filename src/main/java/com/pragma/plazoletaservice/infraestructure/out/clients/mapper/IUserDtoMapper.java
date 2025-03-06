package com.pragma.plazoletaservice.infraestructure.out.clients.mapper;

import com.pragma.plazoletaservice.domain.model.User;
import com.pragma.plazoletaservice.infraestructure.out.clients.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IUserDtoMapper {

    User toUser(UserDto userDto);
}
