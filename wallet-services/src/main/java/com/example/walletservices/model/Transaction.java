package com.example.walletservices.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
@Getter
@Setter
@ToString(exclude = "account") // Exclude account from toString
@EqualsAndHashCode(exclude = "account") // Exclude account from equals/hashCode
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
    @JsonManagedReference // Add this annotation
    private Account account;
}
