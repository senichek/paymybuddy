package com.open.paymybuddy.services;

import java.util.List;

import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.repos.PersonConnectionsRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonConnectionsServiceImpl implements PersonConnectionsService {

    @Autowired
    PersonConnectionsRepo personConnectionRepo;

	@Override
	public List<PersonConnection> getAllByOwnerID(Integer id) {
		return personConnectionRepo.findAllByOwnerID(id);
	}
    
}
