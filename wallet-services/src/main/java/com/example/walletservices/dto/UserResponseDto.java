package com.example.walletservices.dto;

import com.example.walletservices.model.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDto {
    private Integer id;
    private final String email;
    private final String name;
    private List<Account> accounts;

    public UserResponseDto(Integer id, String email, String name, List<Account> accounts) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.accounts = accounts;
    }

}
