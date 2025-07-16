package com.example.walletservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RegistrationDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
}
