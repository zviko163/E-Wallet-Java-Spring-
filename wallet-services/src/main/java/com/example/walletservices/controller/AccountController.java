package com.example.walletservices.controller;

import com.example.walletservices.repository.AccountRepository;
import com.example.walletservices.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @GetMapping("/balance/{accountId}")
    public Double getAccountBalance(Integer accountId) {
        return accountService.calculateAccountBalance(accountId);
    }

    @DeleteMapping("/delete/{accountId}")
    public String deleteAccount(Integer accountId) {
        accountService.deleteAccount(accountId);
        return "Account with ID " + accountId + " has been deleted successfully.";
    }
}
