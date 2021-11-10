package com.open.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.utils.NotEnoughBalanceException;
import com.open.paymybuddy.utils.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MoneyTransactionServiceImplTest {

    @Autowired
    MoneyTransactionService moneyTransactionService;

    @Test
    public void createTest() throws Exception {
        Person sender = new Person();
        sender.setId(1);
        sender.setEmail("james@gmail.com");

        MoneyTransaction expected = new MoneyTransaction("Placeholder", new BigDecimal(50), sender, 
        "james@gmail.com", "carol@gmail.com", new BigDecimal(2.5), LocalDateTime.now());

        MoneyTransaction fromService = moneyTransactionService.create(1, "carol@gmail.com", new BigDecimal(50));

        assertEquals(expected.getSender().getId(), fromService.getSender().getId());
        assertEquals(expected.getSender().getEmail(), fromService.getSender().getEmail());
        assertEquals(expected.getReceiverEmail(), fromService.getReceiverEmail());
        assertEquals(expected.getDescription(), fromService.getDescription());
        assertEquals(expected.getAmount(), fromService.getAmount());
        assertTrue(expected.getTax().compareTo(fromService.getTax()) == 0);
        assertEquals(expected.getDateTime().getYear(), fromService.getDateTime().getYear());
        assertEquals(expected.getDateTime().getDayOfYear(), fromService.getDateTime().getDayOfYear());
        assertEquals(expected.getDateTime().getHour(), fromService.getDateTime().getHour());
        assertEquals(expected.getDateTime().getMinute(), fromService.getDateTime().getMinute());
    }

    @Test
    public void createWithExceptionsTest() throws Exception {
        // For testing the logged in user will have the ID=1.
        // The transfer is made by not the logged-in user.
        Exception exception = assertThrows(Exception.class, () -> moneyTransactionService.create(2, "carol@gmail.com", new BigDecimal(50)));
        assertTrue(exception.getMessage().contains("Data Integrity Exception."));
        // Not enough balance.
        exception = assertThrows(NotEnoughBalanceException.class, () -> moneyTransactionService.create(1, "carol@gmail.com", new BigDecimal(200)));	
        assertTrue(exception.getMessage().contains("Not Enough balance."));
        // Money receiver not found.
        exception = assertThrows(NotFoundException.class, () -> moneyTransactionService.create(1, "arol@gmail.com", new BigDecimal(40)));
        assertTrue(exception.getMessage().contains("Entity with email arol@gmail.com does not exist."));
        // Not friends.
        exception = assertThrows(Exception.class, () -> moneyTransactionService.create(1, "ron@gmail.com", new BigDecimal(40)));
        assertTrue(exception.getMessage().contains("You can only send money to friends."));	
        // Send the negative amount.
        exception = assertThrows(Exception.class, () -> moneyTransactionService.create(1, "carol@gmail.com", new BigDecimal(-10)));	
        assertTrue(exception.getMessage().contains("Amount cannot be null or negative."));	
        // Send zero amount.
        exception = assertThrows(Exception.class, () -> moneyTransactionService.create(1, "carol@gmail.com", new BigDecimal(0)));	
        assertTrue(exception.getMessage().contains("Amount cannot be null or negative."));	
    }
}
