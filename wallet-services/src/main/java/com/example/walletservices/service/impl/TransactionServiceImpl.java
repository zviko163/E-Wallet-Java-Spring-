package com.example.walletservices.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.walletservices.dto.TransactionDto;
import com.example.walletservices.model.Transaction;
import com.example.walletservices.model.TransactionType;
import com.example.walletservices.repository.AccountRepository;
import com.example.walletservices.repository.TransactionRepository;
import com.example.walletservices.service.TransactionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository TRANSACTION_REPOSITORY;
    private final AccountRepository ACCOUNT_REPOSITORY;

    private Double calculateBalance(Integer accountId) {
        List<Transaction> transactions = TRANSACTION_REPOSITORY.findByAccountId(accountId);
        return transactions.stream()
                .mapToDouble(transaction -> 
                    transaction.getTransactionType() == TransactionType.DEPOSIT ? 
                    transaction.getAmount() : 
                    -transaction.getAmount())
                .sum();
    }

    @Override
    public Transaction createTransaction(TransactionDto transactionDto) {
        if(Objects.isNull(transactionDto)){
            throw new IllegalArgumentException("Transaction data cannot be null");
        }

        var account = ACCOUNT_REPOSITORY.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Validate withdrawal amount against current balance
        if (transactionDto.getTransactionType() == TransactionType.WITHDRAWAL) {
            Double currentBalance = calculateBalance(transactionDto.getAccountId());
            if (currentBalance < transactionDto.getAmount()) {
                throw new IllegalStateException("Insufficient funds. Current balance: " + currentBalance);
            }
        }

        // Create a new Transaction object from the DTO
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionDto.getTransactionId());
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAccount(account);

        // Save the transaction to the repository
        Transaction savedTransaction = TRANSACTION_REPOSITORY.save(transaction);

        // Update the account's credit or debit based on the transaction type
        if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
            savedTransaction.getAccount().setCredit(savedTransaction.getAccount().getCredit() + transaction.getAmount());
        } else if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
            savedTransaction.getAccount().setDebit(savedTransaction.getAccount().getDebit() + transaction.getAmount());
        }

        return TRANSACTION_REPOSITORY.save(savedTransaction);
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        if (transactionId == null || transactionId.isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
        
        return TRANSACTION_REPOSITORY.findByTransactionId(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with transaction ID: " + transactionId));
    }

    @Override
    public List<Transaction> findByTransactionTypeAndAccount_Id(TransactionType type, Integer accountId) {
        return TRANSACTION_REPOSITORY.findByTransactionTypeAndAccount_Id(type, accountId);
    }

    @Override
    public Transaction reverseTransaction(String transactionId) {
        Transaction originalTransaction = getTransactionById(transactionId);
        
        // Create reversal transaction with all required fields
        TransactionDto reversalDto = new TransactionDto(
            null, // userId is not needed since it's not part of the Account model
            originalTransaction.getAccount().getId(),
            "REV-" + originalTransaction.getTransactionId(),
            originalTransaction.getTransactionType() == TransactionType.DEPOSIT ?
                TransactionType.WITHDRAWAL : TransactionType.DEPOSIT,
            originalTransaction.getAmount(),
            LocalDateTime.now().toString(),
            "Reversal of transaction: " + originalTransaction.getTransactionId()
        );
        
        return createTransaction(reversalDto);
    }
}
