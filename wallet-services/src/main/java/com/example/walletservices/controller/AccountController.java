package com.example.walletservices.controller;

import com.example.walletservices.model.Account;
import com.example.walletservices.repository.AccountRepository;
import com.example.walletservices.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getAccount/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable Integer accountId) {
        return ResponseEntity.of(accountRepository.findById(accountId));
    }

    @PutMapping("/freezeAccount/{accountId}")
    public void freezeAccount(@PathVariable Integer accountId) {
        accountRepository.freezeAccount(accountId);
        System.out.println("Account with ID " + accountId + " has been freezed successfully.");
    }
}
