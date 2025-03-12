package com.pragma.plazoletaservice.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private String lastName;
    private Long documentId;
    private String phoneNumber;
    private Date birthDate;
    private String password;
    private String email;
    private Long restaurantId;
    private Role role;
}
