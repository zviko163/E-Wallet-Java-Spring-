package com.example.walletservices.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
}
