package com.example.walletservices.service.impl;

import com.example.walletservices.model.Transaction;
import com.example.walletservices.repository.AccountRepository;
import com.example.walletservices.repository.TransactionRepository;
import com.example.walletservices.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public void deleteAccount(Integer accountId) {
        // Check if account exists
        if (!accountRepository.existsById(accountId)) {
            throw new EntityNotFoundException("Account not found with ID: " + accountId);
        }
        
        // Check if account has any balance
        Double balance = calculateAccountBalance(accountId);
        if (balance > 0) {
            throw new IllegalStateException("Cannot delete account with non-zero balance: " + balance);
        }
        
        // Delete all transactions associated with the account
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        transactionRepository.deleteAll(transactions);
        
        // Delete the account
        accountRepository.deleteById(accountId);
    }

    @Override
    public Double calculateAccountBalance(Integer accountId) {
        return transactionRepository.calculateAccountBalance(accountId);
    }
}
