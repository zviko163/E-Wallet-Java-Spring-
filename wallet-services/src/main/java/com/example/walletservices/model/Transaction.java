package com.example.walletservices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String transactionId; // Unique identifier for the transaction
    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // e.g., "credit", "debit"
    private String description; // Optional description of the transaction
    private LocalDateTime timestamp; // ISO 8601 format for date and time

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // Reference to the account associated with the transaction
}
