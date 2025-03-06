package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.domain.model.User;

public interface IUserClientPort {

    User getUserById(Long id);

}
