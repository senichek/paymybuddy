package com.open.paymybuddy.repos;

import javax.transaction.Transactional;

import com.open.paymybuddy.models.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);
    Person findByid(Integer id);
    
    @Query(value = "SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.connections WHERE p.id=:id")
    Person getWithConnections(Integer id);

    @Transactional
	@Modifying
    @Query(value = "DELETE FROM Person p WHERE p.id=:id")
    Integer deleteByPersonID(Integer id);
}