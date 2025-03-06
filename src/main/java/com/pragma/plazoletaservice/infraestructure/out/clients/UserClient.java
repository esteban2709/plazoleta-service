package com.pragma.plazoletaservice.infraestructure.out.clients;

import com.pragma.plazoletaservice.infraestructure.out.clients.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service", url = "localhost:8082/api/v1/users")
public interface UserClient {

    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable(value = "id") Long id);
}
