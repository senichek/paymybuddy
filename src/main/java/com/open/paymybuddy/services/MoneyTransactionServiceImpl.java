package com.open.paymybuddy.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.repos.MoneyTransactionRepo;
import com.open.paymybuddy.repos.PersonRepo;
import com.open.paymybuddy.utils.NotEnoughBalanceException;
import com.open.paymybuddy.utils.NotFoundException;
import com.open.paymybuddy.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// TODO добавить transactional
// TODO потестировать сценарий провала создания транзакции, выбросить экспешн перед сохранением транзакции в бд
// и посмотреть запушится ли в базу данных измененный баланс пользователя.
// для включения транзакций нужна @EnableTransactionManagement на классе где находится main
@Service
public class MoneyTransactionServiceImpl implements MoneyTransactionService {

    @Autowired
    PersonRepo personRepo;

    @Autowired
    MoneyTransactionRepo moneyTransactionRepo;

    @Override
    @Transactional
    public MoneyTransaction create(Integer senderID, String receiverEmail, BigDecimal amount) throws Exception {
        if (SecurityUtil.LOGGED_IN_USER != senderID) {
            throw new Exception("Data Integrity Exception."); //TODO вставить сюда реального залогиненного пользователя
        }
        Person sender = personRepo.findByid(senderID);
        Person receiver = personRepo.findByEmail(receiverEmail);
        // 5% tax
        BigDecimal tax = BigDecimal.valueOf(0.05);
        BigDecimal amountWithTaxIncl = amount.add(amount.multiply(tax));
        if (receiver == null) {
            throw new NotFoundException(String.format("Entity with email %s does not exist.", receiverEmail));
        } else if (!isFriend(sender, receiver)) {
            throw new Exception("You can only send money to friends.");
        }
        // If amount is negative
        else if (amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new Exception("Amount cannot be null or negative.");
            // Checking if balance is not exceeded.
        } else if (sender.getBalance().compareTo(amountWithTaxIncl) < 0) {
            throw new NotEnoughBalanceException("Not Enough balance.");
        } else {
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(amount));
            // Substracting tax of 5%
            sender.setBalance(sender.getBalance().subtract(amount.multiply(tax)));
            MoneyTransaction mtrans = new MoneyTransaction("Placeholder", amount, sender, sender.getEmail(),
                    receiverEmail, amount.multiply(tax), LocalDateTime.now());
            return moneyTransactionRepo.save(mtrans);
        }
    }

    @Override
    public Boolean isFriend(Person one, Person two) {
        Boolean isFriend = false;
        for (PersonConnection con : one.getConnections()) {
            if (con.getEmail().equals(two.getEmail())) {
                isFriend = true;
            }
        }
        return isFriend;
    }

    @Override
    public List<MoneyTransaction> getAllForLoggedIn(String emailOfLoggedInUser) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
