package com.pragma.plazoletaservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String to;
    private String message;
}
