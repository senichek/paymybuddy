package com.open.paymybuddy.services;

import java.math.BigDecimal;
import java.util.List;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.repos.MoneyTransactionRepo;
import com.open.paymybuddy.repos.PersonRepo;
import com.open.paymybuddy.utils.NotEnoughBalanceException;
import com.open.paymybuddy.utils.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoneyTransactionServiceImpl implements MoneyTransactionService {

    @Autowired
    PersonRepo personRepo;

    @Autowired
    MoneyTransactionRepo moneyTransactionRepo;

    @Override
    public MoneyTransaction create(Integer senderID, String receiverEmail, BigDecimal amount)
            throws NotEnoughBalanceException {
                Person sender = personRepo.findByid(senderID);
                Person receiver = personRepo.findByEmail(receiverEmail);
                if (sender == null) {
                    throw new NotFoundException(String.format("Entity with id %s does not exist;", senderID));
                }
                else if (receiver == null) {
                    throw new NotFoundException(String.format("Entity with email %s does not exist;", receiverEmail));
                }
                // If amount is negative
                else if (amount.compareTo(new BigDecimal(0)) <= 0) {
                    throw new RuntimeException("Amount cannot be null or negative.");
                } else if (sender.getBalance().compareTo(amount) < 0) {
                    throw new NotEnoughBalanceException("Not Enough balance");
                } else {
                    sender.setBalance(sender.getBalance().subtract(amount));
                    receiver.setBalance(receiver.getBalance().add(amount));
                    MoneyTransaction mtrans = new MoneyTransaction("Placeholder", amount, sender, sender.getEmail(), receiverEmail);
                    return moneyTransactionRepo.save(mtrans);
                }
            }

    @Override
    public List<MoneyTransaction> getAll() {
        return moneyTransactionRepo.findAll();
    }
}
