package com.open.paymybuddy.services;

import java.util.List;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.repos.PersonRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepo personRepo;

    @Override
    public List<Person> getAll() {
        return personRepo.findAll();
    }
    
}
