package com.open.paymybuddy.services;

import java.util.List;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.repos.PersonRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepo personRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // TODO скорее всего не нужен вообще, удалить в будущем
    @Override
    public List<Person> getAll() {
        return personRepo.findAll();
    }

    @Override
    public Person create(Person person) {
        Person exists = personRepo.findByEmail(person.getEmail());
        if (exists != null) {
            return null;
        } else {
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            return personRepo.save(person);
        }
    }
}
