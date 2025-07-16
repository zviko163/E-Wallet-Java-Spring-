package com.example.walletservices.service.impl;

import com.example.walletservices.dto.LoginRequest;
import com.example.walletservices.dto.RegistrationDto;
import com.example.walletservices.model.Account;
import com.example.walletservices.model.AccountType;
import com.example.walletservices.model.User;
import com.example.walletservices.repository.AccountRepository;
import com.example.walletservices.repository.UserRepository;
import com.example.walletservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository USER_REPOSITORY;
    private final AccountRepository ACCOUNT_REPOSITORY;

    @Override
    public User login(LoginRequest loginRequest) {
        var user = USER_REPOSITORY.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if (!Objects.equals(user.getPassword(), loginRequest.getPassword())) {
            throw new DataIntegrityViolationException("Invalid password");
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
        user.setPassword(registrationDto.getPassword());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setAccounts(new ArrayList<>());

        return USER_REPOSITORY.save(user);
    }

    @Override
    public User addAccount(Integer userId, AccountType accountType) {
        var user = USER_REPOSITORY.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Account account = new Account();
        account.setAccountType(accountType);
        account.setCredit(0.0);
        account.setDebit(0.0);
        account.setCreatedAt(LocalDateTime.now());
        ACCOUNT_REPOSITORY.save(account);
        user.getAccounts().add(account);
        return USER_REPOSITORY.save(user);
    }
}
