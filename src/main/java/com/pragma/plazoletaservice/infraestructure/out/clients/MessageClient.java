package com.pragma.plazoletaservice.infraestructure.out.clients;

import com.pragma.plazoletaservice.infraestructure.out.clients.dto.MessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messaging-service", url = "${feign.client.config.message-service.url}")
public interface MessageClient {

    @PostMapping("/send")
    void sendMessage(@RequestBody MessageDto messageDto);
}
