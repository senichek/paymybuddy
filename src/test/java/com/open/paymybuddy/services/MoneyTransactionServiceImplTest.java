package com.open.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.utils.NotEnoughBalanceException;
import com.open.paymybuddy.utils.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) // Tests change the collection, this is why we have to reset the collection
public class MoneyTransactionServiceImplTest {

    @Autowired
    MoneyTransactionService moneyTransactionService;

    @Test
    @WithUserDetails("james@gmail.com") // see https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html#test-method-withuserdetails
    public void createTest() throws Exception {
        Person sender = new Person();
        sender.setId(1);
        sender.setEmail("james@gmail.com");

        Person receiver = new Person();
        receiver.setId(3);
        receiver.setEmail("carol@gmail.com");

        MoneyTransaction expected = new MoneyTransaction("Description", new BigDecimal(50), sender, 
        receiver, "james@gmail.com", "carol@gmail.com", new BigDecimal(2.5), LocalDateTime.now());

        MoneyTransaction fromService = moneyTransactionService.create(1, "carol@gmail.com", new BigDecimal(50), "Description");

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
    @WithUserDetails("james@gmail.com")
    public void createWithExceptionsTest() throws Exception {
        // For testing the logged in user will have the ID=1.
        // The transfer is made by not the logged-in user.
        Exception exception = assertThrows(Exception.class, () -> moneyTransactionService.create(2, "carol@gmail.com", new BigDecimal(50), "Description"));
        assertTrue(exception.getMessage().contains("Data Integrity Exception."));
        // Not enough balance.
        exception = assertThrows(NotEnoughBalanceException.class, () -> moneyTransactionService.create(1, "carol@gmail.com", new BigDecimal(200), "Description"));	
        assertTrue(exception.getMessage().contains("Not Enough balance."));
        // Money receiver not found.
        exception = assertThrows(NotFoundException.class, () -> moneyTransactionService.create(1, "arol@gmail.com", new BigDecimal(40), "Description"));
        assertTrue(exception.getMessage().contains("Entity with email arol@gmail.com does not exist."));
        // Not friends.
        exception = assertThrows(Exception.class, () -> moneyTransactionService.create(1, "ron@gmail.com", new BigDecimal(40), "Description"));
        assertTrue(exception.getMessage().contains("You can only send money to friends."));	
        // Send the negative amount.
        exception = assertThrows(Exception.class, () -> moneyTransactionService.create(1, "carol@gmail.com", new BigDecimal(-10), "Description"));	
        assertTrue(exception.getMessage().contains("Amount cannot be null or negative."));	
        // Send zero amount.
        exception = assertThrows(Exception.class, () -> moneyTransactionService.create(1, "carol@gmail.com", new BigDecimal(0), "Description"));	
        assertTrue(exception.getMessage().contains("Amount cannot be null or negative."));	
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void getAllForLoggedInTest() throws Exception {
        // We know that there are 10 transactions for james@gmail.com
        List<MoneyTransaction> allForLoggedIn = moneyTransactionService.getAllForLoggedIn("james@gmail.com");
        assertEquals(10, allForLoggedIn.size());
        // Comparing some of the values.
        allForLoggedIn.forEach(mTrans -> {
            assertEquals("james@gmail.com", mTrans.getSenderEmail());
            assertEquals("carol@gmail.com", mTrans.getReceiverEmail());
            assertEquals(BigDecimal.valueOf(20.0), mTrans.getAmount());
        });
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void getAllForLoggedInWithExceptionTest() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> moneyTransactionService.getAllForLoggedIn("carol@gmail.com"));
        assertTrue(exception.getMessage().contains("Data Integrity Exception."));
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void findPaginatedTest() throws Exception {
        List<MoneyTransaction> allForLoggedIn = moneyTransactionService.getAllForLoggedIn("james@gmail.com");
        // It must return the transaction with ID 6, 7 and 8.
        Page<MoneyTransaction> findPaginated = moneyTransactionService.findPaginated(PageRequest.of(1, 3), allForLoggedIn);
        assertEquals(8, findPaginated.getContent().get(0).getId());
        assertEquals(7, findPaginated.getContent().get(1).getId());
        assertEquals(6, findPaginated.getContent().get(2).getId());
    }
}
