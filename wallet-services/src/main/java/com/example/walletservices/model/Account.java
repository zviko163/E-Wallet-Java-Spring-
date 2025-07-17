package com.example.walletservices.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    private Double credit;
    private Double debit;
    
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)  // Change from LAZY to EAGER
    @JoinColumn(name = "user_id")
    private User user;
}
