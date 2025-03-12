package com.pragma.plazoletaservice.infraestructure.out.jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "order_dishes")
public class OrderDishEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "orderdish_id", nullable = false)
    private Long id;

    @JsonIgnoreProperties(value = { "order" })
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id", nullable = false)
    private DishEntity dish;
    private Integer quantity;
//    private Long subTotalPrice;
//
//    @PrePersist
//    protected void onCreate() {
//        if (subTotalPrice == null) {
//            subTotalPrice = dish.getPrice() * quantity;
//        }
//    }
}
