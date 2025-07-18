package com.example.walletservices.repository;

import com.example.walletservices.model.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
//    BigDecimal getAccountBalance(Integer accountId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE accounts
            SET account_status = 'FROZEN'
        WHERE id = :accountId
    """, nativeQuery = true)

    void freezeAccount(@Param("accountId") Integer accountId);
}
