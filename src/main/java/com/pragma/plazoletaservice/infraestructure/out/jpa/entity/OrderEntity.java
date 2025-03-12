package com.pragma.plazoletaservice.infraestructure.out.jpa.entity;

import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    private Long id;

    private Long clientId;
    private Long chefId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    private OrderStatus orderStatus;
    private String securityCode;

    @PrePersist
    protected void onCreate() {
        if (orderStatus == null) {
            orderStatus = OrderStatus.PENDING;
        }
    }
}
