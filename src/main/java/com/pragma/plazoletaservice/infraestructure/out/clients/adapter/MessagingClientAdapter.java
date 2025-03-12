package com.pragma.plazoletaservice.infraestructure.out.clients.adapter;

import com.pragma.plazoletaservice.domain.model.Message;
import com.pragma.plazoletaservice.domain.spi.IMessagingClientPort;
import com.pragma.plazoletaservice.infraestructure.out.clients.MessageClient;
import com.pragma.plazoletaservice.infraestructure.out.clients.mapper.IMessageDtoMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessagingClientAdapter implements IMessagingClientPort {

    private final MessageClient messageClient;
    private final IMessageDtoMapper messageDtoMapper;

    @Override
    public void sendMessage(Message message) {
        try {
            messageClient.sendMessage(messageDtoMapper.toMessageDto(message));
        } catch (Exception e) {
            // Handle the exception as needed
            throw new RuntimeException("Failed to send message");
        }
    }
}
