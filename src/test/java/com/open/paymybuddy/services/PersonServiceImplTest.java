package com.open.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonServiceImplTest {

    @Autowired
    PersonService personService;

    @Test
    public void createTest() {
        Person expected = new Person("John", "j@gmail.com", "11111", null, null, null);
        Person fromService = personService.create(new Person("John", "j@gmail.com", "11111", null, null, null));
        assertEquals(expected, fromService);
    }

}
