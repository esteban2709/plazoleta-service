package com.pragma.plazoletaservice.infraestructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "restaurants")
@Data
@NoArgsConstructor
@Entity
public class RestaurantEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "restaurant_id", nullable = false)
    private Long id;
    private String name;
    private Long nit;
    private String address;
    @Column( length = 13)
    private String phone;
    @Column(nullable = false, name = "url_logo")
    private String urlLogo;
    @Column(nullable = false, name = "owner_id")
    private Long ownerId;

}
