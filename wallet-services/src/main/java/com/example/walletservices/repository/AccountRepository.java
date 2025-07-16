package com.example.walletservices.repository;

import com.example.walletservices.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
//    BigDecimal getAccountBalance(Integer accountId);
}
