package com.open.paymybuddy.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.repos.PersonRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepo personRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // TODO delete later
    @Override
    public List<Person> getAll() {
        return personRepo.findAll();
    }

    @Override
    @Transactional
    public Person create(Person person) {
        Person exists = personRepo.findByEmail(person.getEmail());
        Person created;
        if (exists != null) {
            log.info("User with email {} exists.", person.getEmail());
            return null;
        } else {
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            person.setBalance(new BigDecimal(0));
            created = personRepo.save(person);
            log.info("Created {}.", created);
            return created;
        }
    }
}
