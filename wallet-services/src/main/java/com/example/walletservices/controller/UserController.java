package com.example.walletservices.controller;

import com.example.walletservices.dto.LoginRequest;
import com.example.walletservices.dto.RegistrationDto;
import com.example.walletservices.model.AccountType;
import com.example.walletservices.model.User;
import com.example.walletservices.repository.UserRepository;
import com.example.walletservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Qualifier("userServiceImpl")
    private final UserService USER_SERVICE;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest user) {
        return  ResponseEntity.ok(USER_SERVICE.login(user));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegistrationDto user) {
        return ResponseEntity.ok(USER_SERVICE.register(user));
    }

    @PutMapping("/addAccount/{id}")
    public ResponseEntity<User> addAccount(@PathVariable Integer id, @RequestParam AccountType accountType) {
        return ResponseEntity.ok(USER_SERVICE.addAccount(id, accountType));
    }

}
