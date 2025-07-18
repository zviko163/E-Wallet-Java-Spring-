package com.example.walletservices.repository;

import com.example.walletservices.model.Transaction;
import com.example.walletservices.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByAccountId(Integer accountId);
    Optional<Transaction> findByTransactionId(String transactionId);
    List<Transaction> findByTransactionTypeAndAccount_Id(TransactionType type, Integer accountId);

    @Query(value = """
        SELECT 
            COALESCE(SUM(CASE WHEN transaction_type = 'DEPOSIT' THEN amount ELSE 0 END), 0) -
            COALESCE(SUM(CASE WHEN transaction_type = 'WITHDRAWAL' THEN amount ELSE 0 END), 0)
        FROM transactions
        WHERE account_id = :accountId
    """, nativeQuery = true)
    Double calculateAccountBalance(@Param("accountId") Integer accountId);
}
