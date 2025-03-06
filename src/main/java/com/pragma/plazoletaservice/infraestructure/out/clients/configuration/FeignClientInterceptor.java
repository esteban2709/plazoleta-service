package com.pragma.plazoletaservice.infraestructure.out.clients.configuration;

import com.pragma.plazoletaservice.infraestructure.security.TokenUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = TokenUtils.getCompleteTokenFromRequest();
        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}
