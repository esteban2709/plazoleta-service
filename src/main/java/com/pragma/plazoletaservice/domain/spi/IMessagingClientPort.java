package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.domain.model.Message;

public interface IMessagingClientPort {

    void sendMessage(Message message);
}
