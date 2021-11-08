package com.open.paymybuddy.repos;

import com.open.paymybuddy.models.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);
    Person findByid(Integer id);
}
