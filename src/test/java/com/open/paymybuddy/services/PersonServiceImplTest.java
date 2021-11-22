package com.open.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.utils.NotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) // Tests change the collection, this is why we have to reset the collection
public class PersonServiceImplTest {

    @Autowired
    PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    

    @Test
    public void createTest() {
        Person expected = new Person("John", "j@gmail.com", "11111", null, null, null);
        Person fromService = personService.create(new Person("John", "j@gmail.com", "11111", null, null, null));
        assertEquals(expected.getName(), fromService.getName());
        assertEquals(expected.getName(), fromService.getName());
        assertNotNull(fromService.getId());
        assertNotNull(fromService.getPassword());
        // password is encypted, so we do not compare it to the Expected.
    }

    @Test
    public void createExistsTest() {
        // if User already exists in db we return null.
        assertNull(personService.create(new Person("james", "james@gmail.com", "11111", null, null, null)));
    }

    @Test
    @WithUserDetails("james@gmail.com") // see https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html#test-method-withuserdetails
    public void update() throws Exception {
        Person toUpdate = new Person();
        // Person with ID=1 exists in DB.
        toUpdate.setId(1);
        toUpdate.setName("updated");
        toUpdate.setEmail("updated@gmail.com");
        String encodedPass = passwordEncoder.encode("11111");
        toUpdate.setPassword(encodedPass);

        Person fromService = personService.update(toUpdate);

        assertEquals("updated", fromService.getName());
        assertEquals("updated@gmail.com", fromService.getEmail());
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void updateWithException() throws Exception {
        Person prs = new Person();
        // ID of the logged-in user (ID=1) is different from the one we want to update.
        prs.setId(2);
        Exception exception = assertThrows(Exception.class, () -> personService.update(prs));
        assertTrue(exception.getMessage().contains("Data Integrity Exception."));
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void updateSameDetails() throws Exception {
        Person toUpdate = new Person();
        // Person with ID=1 exists in DB.
        // Set up the same details that exists in DB.
        toUpdate.setId(1);
        toUpdate.setName("James");
        toUpdate.setEmail("james@gmail.com");
        toUpdate.setPassword("$2a$04$GGTsxpDhsbZS9gDKUUpqPOxXuc/9PU1h56ueiFHk68PhY0flyJJmy");

        Person fromService = personService.update(toUpdate);

        assertEquals(toUpdate.getName(), fromService.getName());
        assertEquals(toUpdate.getEmail(), fromService.getEmail());
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void updateNoDetails() throws Exception {
        Person toUpdate = new Person();
        // Person with ID=1 exists in DB.
        // If the details were not changed, we return the unchanged 
        // user (the one who islogged in).
        toUpdate.setId(1);
        toUpdate.setName("");
        toUpdate.setEmail("");
        toUpdate.setPassword("");

        Person fromService = personService.update(toUpdate);

        assertEquals("James", fromService.getName());
        assertEquals("james@gmail.com", fromService.getEmail());
    }

    @Test
    public void findByIdTest() throws NotFoundException {
        Person expected = new Person();
        expected.setId(1);
        expected.setName("James");
        expected.setEmail("james@gmail.com");
        expected.setBalance(BigDecimal.valueOf(120.0));
        expected.setPassword("$2a$04$GGTsxpDhsbZS9gDKUUpqPOxXuc/9PU1h56ueiFHk68PhY0flyJJmy");

        Person fromService = personService.findById(1);
        assertEquals(expected.getName(), fromService.getName());
        assertEquals(expected.getEmail(), fromService.getEmail());
        assertEquals(expected.getPassword(), fromService.getPassword());
        assertEquals(expected.getBalance(), fromService.getBalance());
    }

    @Test
    public void findByIdExceptionTest() {
        Exception exception = assertThrows(NotFoundException.class, () -> personService.findById(125));
        assertTrue(exception.getMessage().contains("Entity with id 125 does not exist."));
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void increaseBalanceTest() throws Exception {
        Person expected = new Person("James", "james@gmail.com", "11111", BigDecimal.valueOf(140.0), null, null);
        expected.setId(1);
        // We will increase the balance of user with ID=1 who is present in DB.
        Person fromService = new Person();
        fromService.setId(1);
        fromService.setBalance(BigDecimal.valueOf(20.0));
        fromService = personService.increaseBalance(fromService);
        assertEquals(expected.getBalance(), fromService.getBalance());
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void increaseBalanceWithExceptionTest() throws Exception {
        Person prs = new Person();
        prs.setId(2);
        Exception exception = assertThrows(Exception.class, () -> personService.increaseBalance(prs));
        assertTrue(exception.getMessage().contains("Data Integrity Exception."));

        prs.setId(1);
        prs.setBalance(BigDecimal.valueOf(0));
        exception = assertThrows(Exception.class, () -> personService.increaseBalance(prs));
        assertTrue(exception.getMessage().contains("Amount cannot be null, zero or negative."));
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void decreaseBalanceTest() throws Exception {
        Person expected = new Person("James", "james@gmail.com", "11111", BigDecimal.valueOf(100.0), null, null);
        expected.setId(1);
        // We will decrease the balance of user with ID=1 who is present in DB.
        Person fromService = new Person();
        fromService.setId(1);
        fromService.setBalance(BigDecimal.valueOf(20.0));
        fromService = personService.decreaseBalance(fromService);
        assertEquals(expected.getBalance(), fromService.getBalance());
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void decreaseBalanceWithExceptionTest() throws Exception {
        Person prs = new Person();
        prs.setId(2);
        Exception exception = assertThrows(Exception.class, () -> personService.decreaseBalance(prs));
        assertTrue(exception.getMessage().contains("Data Integrity Exception."));

        prs.setId(1);
        prs.setBalance(new BigDecimal(0));
        exception = assertThrows(Exception.class, () -> personService.decreaseBalance(prs));
        assertTrue(exception.getMessage().contains("Amount cannot be null, zero or negative."));
        
        // The user with ID=1 has the balance of 120, we try to payout 121.
        prs.setBalance(BigDecimal.valueOf(121));
        exception = assertThrows(Exception.class, () -> personService.decreaseBalance(prs));
        assertTrue(exception.getMessage().contains("Not enough balance for payout. Decrease the payout amount."));
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void deleteTest() throws NotFoundException {
        Person fromService = personService.delete(3);
        assertEquals(3, fromService.getId());
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void deleteWithExceptionTest() throws NotFoundException {
        Exception exception = assertThrows(NotFoundException.class, () -> personService.delete(20));
        assertTrue(exception.getMessage().contains("Entity with id 20 does not exist."));
    }
}
