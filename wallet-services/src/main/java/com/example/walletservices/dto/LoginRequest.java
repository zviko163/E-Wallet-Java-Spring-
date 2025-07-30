package com.example.walletservices.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
public class LoginRequest {
    private String email;
    private String password;
}
