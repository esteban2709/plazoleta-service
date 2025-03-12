package com.pragma.plazoletaservice.infraestructure.out.clients.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String name;
    private String lastName;
    private Long documentId;
    private String phoneNumber;
    private Date birthDate;
    private String password;
    private String email;
    private Long restaurantId;
    private RoleDto role;

}
