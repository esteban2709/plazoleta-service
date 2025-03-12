package com.pragma.plazoletaservice.infraestructure.out.clients.mapper;

import com.pragma.plazoletaservice.domain.model.User;
import com.pragma.plazoletaservice.infraestructure.out.clients.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IUserDtoMapper {

    @Mapping(target = "restaurantId", source = "restaurantId")
    User toUser(UserDto userDto);
}
