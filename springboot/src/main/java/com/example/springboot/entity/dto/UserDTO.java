package com.example.springboot.entity.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String password;
    private String token;
}