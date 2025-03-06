package com.pragma.plazoletaservice.infraestructure.out.clients.adapter;

import com.pragma.plazoletaservice.domain.model.User;
import com.pragma.plazoletaservice.domain.spi.IUserClientPort;
import com.pragma.plazoletaservice.infraestructure.exception.UserNotFoundException;
import com.pragma.plazoletaservice.infraestructure.out.clients.UserClient;
import com.pragma.plazoletaservice.infraestructure.out.clients.mapper.IUserDtoMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class UserClientAdapter implements IUserClientPort {

    private final UserClient userClient;
    private final IUserDtoMapper userDtoMapper;

    public User getUserById(Long id) {
        try {
            return userDtoMapper.toUser(userClient.getUserById(id));
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundException();
        }
    }
}
