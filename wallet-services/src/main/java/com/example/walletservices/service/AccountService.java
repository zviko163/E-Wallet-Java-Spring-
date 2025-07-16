package com.example.walletservices.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface AccountService {
    // Account updateAccount(Integer accountId, AccountDto accountDto);
    void deleteAccount(Integer accountId);
    Double calculateAccountBalance(Integer accountId);
}
