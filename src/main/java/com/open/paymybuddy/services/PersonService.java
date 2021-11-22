package com.open.paymybuddy.services;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.utils.NotFoundException;

public interface PersonService {
    Person create(Person person);
    Person update(Person person) throws Exception;
    Person findById(Integer id) throws NotFoundException;
    Person increaseBalance(Person person) throws Exception;
    Person decreaseBalance(Person person) throws Exception;
    Person delete(Integer id) throws NotFoundException;
}
