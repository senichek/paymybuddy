package com.open.paymybuddy.services;

import java.util.List;

import javax.transaction.Transactional;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.repos.PersonConnectionsRepo;
import com.open.paymybuddy.repos.PersonRepo;
import com.open.paymybuddy.utils.NotFoundException;
import com.open.paymybuddy.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PersonConnectionsServiceImpl implements PersonConnectionsService {

	@Autowired
	private PersonConnectionsRepo personConnectionRepo;

	@Autowired
	private PersonRepo personRepo;

	@Autowired
	private SecurityUtil securityUtil;

	@Override
	public List<PersonConnection> getAllByOwnerID(Integer id) {
		return personConnectionRepo.findAllByOwnerID(id);
	}

	@Override
	@Transactional
	public PersonConnection create(Integer ownerID, String friendsEmail) throws Exception {
		PersonConnection personConnection = new PersonConnection();
		Person friend = personRepo.findByEmail(friendsEmail);
		if (securityUtil.getLoggedInUser().getId() != ownerID) {
			throw new Exception("Data Integrity Exception.");
			// You can add to connections (to firends) only users that exist in DB, i.e. the
			// registered ones.
		} else if (friend == null) {
			throw new NotFoundException(String.format("Entity with email %s does not exist.", friendsEmail));
		} else {
			Person owner = personRepo.findByid(ownerID);
			if (!isPresentInConnections(owner, friend)) {
				friend = personRepo.findByEmail(friendsEmail);
				personConnection = new PersonConnection(friendsEmail, owner, friend);
				personConnectionRepo.save(personConnection);
				log.info("Created {}.", personConnection);
			}
			return personConnection;
		}
	}

	@Override
	public PersonConnection deleteByConnectionID(Integer id) throws NotFoundException {
		PersonConnection pConnection = personConnectionRepo.findByid(id);
		if (pConnection == null) {
			throw new NotFoundException(String.format("Entity with id %s does not exist.", id));
		}
		 else {
			personConnectionRepo.deleteByConnectionID(pConnection.getId());
			log.info("Deleted {}.", pConnection);
			return pConnection;
		}
	}

	@Override
	public Boolean isPresentInConnections(Person owner, Person friend) {
		Boolean match = false;
		// The logged-in user cannot add himself to his own connections.
		// We consider him as being present in the friend's list of his own by default
		// to prevent his from being added to his own friend's (connections) list.
		if (friend.getEmail().equals(securityUtil.getLoggedInUser().getEmail())) {
			match = true;
		}
		for (PersonConnection con : owner.getConnections()) {
			if (con.getEmail().equals(friend.getEmail())) {
				match = true;
			}
		}
		return match;
	}
}
