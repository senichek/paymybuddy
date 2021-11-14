package com.open.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.open.paymybuddy.models.Person;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

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

    @Test
    public void createWithExceptionTest() {
        //Email must be unique, Database constraint.
        personService.create(new Person("John", "j@gmail.com", "11111", null, null, null));
        assertThrows(DataIntegrityViolationException.class, () -> personService.create(new Person("John", "j@gmail.com", "11111", null, null, null)));
    }
}
