package com.open.paymybuddy.services;

import java.math.BigDecimal;
import java.util.List;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MoneyTransactionService {
    MoneyTransaction create(Integer senderID, String receiverEmail, BigDecimal amount, String description) throws Exception;
    List<MoneyTransaction> getAllForLoggedIn(String emailOfLoggedInUser) throws Exception;
    Boolean isFriend(Person one, Person two);
    Page<MoneyTransaction> findPaginated(Pageable pageable, List<MoneyTransaction> transactions);
}
