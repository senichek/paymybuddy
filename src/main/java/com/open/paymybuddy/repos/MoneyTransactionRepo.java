package com.open.paymybuddy.repos;

import com.open.paymybuddy.models.MoneyTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyTransactionRepo extends JpaRepository<MoneyTransaction, Integer> {
}
