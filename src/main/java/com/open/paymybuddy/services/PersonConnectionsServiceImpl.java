package com.open.paymybuddy.services;

import java.util.List;

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
	PersonConnectionsRepo personConnectionRepo;

	@Autowired
	PersonRepo personRepo;

	@Override
	public List<PersonConnection> getAllByOwnerID(Integer id) {
		return personConnectionRepo.findAllByOwnerID(id);
	}

	@Override
	public PersonConnection create(Integer ownerID, String friendsEmail) throws Exception {
		// TODO добавить проверку на то есть ли друг уже в списке друзей, если есть,
		// то ничего не делать или добавить снова, но чтобы в списке не было дубликатов.
		if (SecurityUtil.getLoggedInUser().getId() != ownerID) {
			throw new Exception("Data Integrity Exception.");
			// You can add to connections (to firends) only users that exist in DB, i.e. the
			// registered ones.
		} else if (personRepo.findByEmail(friendsEmail) == null) {
			throw new NotFoundException(String.format("Entity with email %s does not exist.", friendsEmail));
		} else {
			Person owner = personRepo.findByid(ownerID);
			Person friend = personRepo.findByEmail(friendsEmail);
			PersonConnection personConnection = new PersonConnection(friendsEmail, owner, friend);
			log.info("Created {}.", personConnection);
			return personConnectionRepo.save(personConnection);
		}
	}
}
