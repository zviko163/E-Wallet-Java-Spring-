package com.example.walletservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateDto {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
}
