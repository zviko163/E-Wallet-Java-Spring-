package com.example.walletservices.service;

import com.example.walletservices.dto.LoginRequest;
import com.example.walletservices.dto.RegistrationDto;
import com.example.walletservices.dto.UserUpdateDto;
import com.example.walletservices.model.AccountType;
import com.example.walletservices.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User login(LoginRequest loginRequest);
    User register(RegistrationDto registrationDto);
    User addAccount(Integer userId, AccountType accountType);
    User findById(Integer id);
    List<User> findAll();
    User updateUser(Integer id, UserUpdateDto user);
}
