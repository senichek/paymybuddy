package com.open.paymybuddy.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MoneyTransactionServiceImpl implements MoneyTransactionService {

    @Autowired
    PersonRepo personRepo;

    @Autowired
    MoneyTransactionRepo moneyTransactionRepo;

    @Override
    @Transactional
    public MoneyTransaction create(Integer senderID, String receiverEmail, BigDecimal amount, String description) throws Exception {
        if (SecurityUtil.getLoggedInUser().getId() != senderID) {
            throw new Exception("Data Integrity Exception.");
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
            MoneyTransaction mtrans = new MoneyTransaction(description, amount, sender, receiver, sender.getEmail(),
                    receiverEmail, amount.multiply(tax), LocalDateTime.now());
            log.info("Created {}.", mtrans);
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
    public List<MoneyTransaction> getAllForLoggedIn(String emailOfLoggedInUser) throws Exception {
        if (SecurityUtil.getLoggedInUser().getEmail() != emailOfLoggedInUser) {
            throw new Exception("Data Integrity Exception.");
        }
        List<MoneyTransaction> result = moneyTransactionRepo.getAllForLoggedIn(SecurityUtil.getLoggedInUser().getId());
        Collections.sort(result, new Comparator<MoneyTransaction>() {
            public int compare(MoneyTransaction o1, MoneyTransaction o2) {
                return o2.getDateTime().compareTo(o1.getDateTime());
            }
          });
          return result;
    }

    @Override
    public Page<MoneyTransaction> findPaginated(Pageable pageable, List<MoneyTransaction> transactions) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<MoneyTransaction> list;

        if (transactions.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, transactions.size());
            list = transactions.subList(startItem, toIndex);
        }

        Page<MoneyTransaction> transactionPage
          = new PageImpl<MoneyTransaction>(list, PageRequest.of(currentPage, pageSize), transactions.size());

        return transactionPage;
    }
}
