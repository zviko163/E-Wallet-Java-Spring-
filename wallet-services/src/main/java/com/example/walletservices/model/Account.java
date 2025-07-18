package com.example.walletservices.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
@Getter
@Setter
@ToString(exclude = "user") // Exclude user from toString
@EqualsAndHashCode(exclude = "user") // Exclude user from equals/hashCode
public class Account {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    private Double credit;
    private Double debit;
    
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference // Add this annotation
    private User user;
}
