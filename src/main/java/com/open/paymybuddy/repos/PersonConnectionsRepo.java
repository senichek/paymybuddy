package com.open.paymybuddy.repos;

import java.util.List;

import com.open.paymybuddy.models.PersonConnection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonConnectionsRepo extends JpaRepository<PersonConnection, Integer> {
    @Query(value = "SELECT DISTINCT p FROM PersonConnection p WHERE p.person.id=:id")
    List<PersonConnection> findAllByOwnerID(Integer id);
}