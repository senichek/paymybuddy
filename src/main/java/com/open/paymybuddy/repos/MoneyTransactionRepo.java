package com.open.paymybuddy.repos;

import java.util.List;

import com.open.paymybuddy.models.MoneyTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyTransactionRepo extends JpaRepository<MoneyTransaction, Integer> {
    @Query(value = "SELECT DISTINCT m FROM MoneyTransaction m WHERE m.sender.id=:id")
    List<MoneyTransaction> getAllForLoggedIn(Integer id);
}
