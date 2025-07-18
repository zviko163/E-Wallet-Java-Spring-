package com.example.walletservices.controller;

import com.example.walletservices.dto.LoginRequest;
import com.example.walletservices.dto.RegistrationDto;
import com.example.walletservices.dto.UserUpdateDto;
import com.example.walletservices.model.AccountType;
import com.example.walletservices.model.User;
import com.example.walletservices.repository.UserRepository;
import com.example.walletservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/findAll")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(USER_SERVICE.findAll());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(USER_SERVICE.findById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserUpdateDto user) {
        return ResponseEntity.ok(USER_SERVICE.updateUser(id, user));
    }

}
