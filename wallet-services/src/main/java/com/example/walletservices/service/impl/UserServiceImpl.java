package com.example.walletservices.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.walletservices.dto.UserUpdateDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.walletservices.dto.LoginRequest;
import com.example.walletservices.dto.RegistrationDto;
import com.example.walletservices.model.Account;
import com.example.walletservices.model.AccountType;
import com.example.walletservices.model.User;
import com.example.walletservices.repository.AccountRepository;
import com.example.walletservices.repository.UserRepository;
import com.example.walletservices.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository USER_REPOSITORY;
    private final AccountRepository ACCOUNT_REPOSITORY;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(LoginRequest loginRequest) {
        var user = USER_REPOSITORY.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        // checking for password matching using passwordEncoder.matches method
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
           throw new DataIntegrityViolationException("Password does not match");
        }

        return user;
    }

    @Override
    public User register(RegistrationDto registrationDto) {
        if (USER_REPOSITORY.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setAccounts(new ArrayList<>());

        return USER_REPOSITORY.save(user);
    }

    @Override
    public User addAccount(Integer userId, AccountType accountType) {
        var user = USER_REPOSITORY.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user already has an account of this type
        boolean hasAccountType = user.getAccounts().stream()
                .anyMatch(account -> account.getAccountType() == accountType);
        
        if (hasAccountType) {
            throw new IllegalStateException("User already has an account of type: " + accountType);
        }

        Account account = new Account();
        account.setUser(user);
        account.setAccountType(accountType);
        account.setCredit(0.0);
        account.setDebit(0.0);
        account.setCreatedAt(LocalDateTime.now());
        ACCOUNT_REPOSITORY.save(account);
        user.getAccounts().add(account);
        return USER_REPOSITORY.save(user);
    }

    @Override
    public List<User> findAll() {
        return USER_REPOSITORY.findAll();
    }

    @Override
    public User updateUser(Integer id, UserUpdateDto user) {
        User existingUser = USER_REPOSITORY.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user details
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setPhoneNumber(user.getPhoneNumber());

        return USER_REPOSITORY.save(existingUser);
    }

    @Override
    public User findById(Integer id) {
        return USER_REPOSITORY.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
