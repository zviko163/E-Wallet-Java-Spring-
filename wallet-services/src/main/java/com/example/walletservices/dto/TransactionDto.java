package com.example.walletservices.dto;

import com.example.walletservices.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDto {
    private Integer userId;
    private Integer accountId;
    private String transactionId;
    private TransactionType transactionType;
    private Double amount;
    private String timestamp;
    private String description;
}
