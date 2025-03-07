package com.pragma.plazoletaservice.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Order {

    private Long id;
    private List<OrderDetail> detailList;
    private Long totalPrice;
    private String status;
    private User client;
    private User employee;
    private Restaurant restaurant;
}
