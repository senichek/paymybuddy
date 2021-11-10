package com.open.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.models.PersonConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonСonnectionsServiceImplTest {

    @Autowired
    PersonConnectionsService personConnectionsService;

    @Test
    public void getAllByOwnerIDTest() {
        PersonConnection conn1 = new PersonConnection("mike@gmail.com", new Person());
        PersonConnection conn2 = new PersonConnection("carol@gmail.com", new Person());
        List<PersonConnection> expected = List.of(conn1, conn2);
        List<PersonConnection> fromService = personConnectionsService.getAllByOwnerID(1);
        // Lazy fetching, this is why we do not compare Person inside of a Connection,
        // we compare only emails.
        for (int i =0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getEmail(), fromService.get(i).getEmail());
        }
    }

}
