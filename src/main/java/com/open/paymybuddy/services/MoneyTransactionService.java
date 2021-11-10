package com.open.paymybuddy.services;

import java.math.BigDecimal;
import java.util.List;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;

public interface MoneyTransactionService {
    MoneyTransaction create(Integer senderID, String receiverEmail, BigDecimal amount) throws Exception;
    List<MoneyTransaction> getAll();
    Boolean isFriend(Person one, Person two);
}
