package com.open.paymybuddy.services;

import java.util.List;

import com.open.paymybuddy.models.Person;

public interface PersonService {
    List<Person> getAll();
    Person create(Person person);
}
