package com.example.walletservices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.walletservices.dto.TransactionDto;
import com.example.walletservices.model.Transaction;
import com.example.walletservices.model.TransactionType;

@Service
public interface TransactionService {
    Transaction createTransaction(TransactionDto transactionDto);
    Transaction getTransactionById(String transactionId);
    List<Transaction> findByTransactionTypeAndAccount_Id(TransactionType type, Integer accountId);
    Transaction reverseTransaction(String transactionId);
}
