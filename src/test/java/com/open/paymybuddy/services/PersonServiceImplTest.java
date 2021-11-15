package com.open.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
}
