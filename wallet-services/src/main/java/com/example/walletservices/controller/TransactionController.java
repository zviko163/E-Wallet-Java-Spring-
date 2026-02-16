package com.example.walletservices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.walletservices.dto.TransactionDto;
import com.example.walletservices.model.Transaction;
import com.example.walletservices.model.TransactionType;
import com.example.walletservices.service.TransactionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

//@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Qualifier("transactionService")
    private final TransactionService TRANSACTION_SERVICE;

    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDto transaction) {
        Transaction createdTransaction = TRANSACTION_SERVICE.createTransaction(transaction);
        return ResponseEntity.ok(createdTransaction);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String transactionId) {
        Transaction transaction = TRANSACTION_SERVICE.getTransactionById(transactionId);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transaction);
        }
    }

    @GetMapping("/type/{type}/account/{accountId}")
    public ResponseEntity<List<Transaction>> findByTransactionTypeAndAccount_Id(@PathVariable TransactionType type, @PathVariable Integer accountId) {
        List<Transaction> transactions = TRANSACTION_SERVICE.findByTransactionTypeAndAccount_Id(type, accountId);
        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transactions);
        }
    }

    @PostMapping("/{transactionId}/reverse")
    public ResponseEntity<Transaction> reverseTransaction(@PathVariable String transactionId) {
        try {
            Transaction reversedTransaction = TRANSACTION_SERVICE.reverseTransaction(transactionId);
            return ResponseEntity.ok(reversedTransaction);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferBetweenAccounts(
            @RequestParam Integer fromAccountId,
            @RequestParam Integer toAccountId,
            @RequestParam Double amount) {
        try {
            Transaction transaction = TRANSACTION_SERVICE.transferBetweenAccounts(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok(transaction);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
