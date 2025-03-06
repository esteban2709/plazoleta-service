package com.pragma.plazoletaservice.infraestructure.out.utils;

import com.pragma.plazoletaservice.domain.spi.ITokenUtilsPort;
import com.pragma.plazoletaservice.infraestructure.security.TokenUtils;

public class TokenUtilsAdapter implements ITokenUtilsPort {

    @Override
    public String getRole() {
        return TokenUtils.getRoleIdFromToken();
    }

    @Override
    public Long getUserId() {
        return TokenUtils.getUserIdFromToken();
    }
}
