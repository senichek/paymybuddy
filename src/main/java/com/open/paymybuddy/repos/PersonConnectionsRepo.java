package com.open.paymybuddy.repos;

import java.util.List;

import javax.transaction.Transactional;

import com.open.paymybuddy.models.PersonConnection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonConnectionsRepo extends JpaRepository<PersonConnection, Integer> {
    @Query(value = "SELECT DISTINCT p FROM PersonConnection p WHERE p.owner.id=:id")
    List<PersonConnection> findAllByOwnerID(Integer id);

    @Transactional
	@Modifying
    @Query(value = "DELETE FROM PersonConnection p WHERE p.id=:id")
    Integer deleteByConnectionID(Integer id);

    PersonConnection findByid(Integer id);
}
