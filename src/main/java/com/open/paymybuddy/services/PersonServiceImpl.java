package com.open.paymybuddy.services;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.repos.PersonRepo;
import com.open.paymybuddy.utils.NotFoundException;
import com.open.paymybuddy.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    @Transactional
    public Person create(Person person) {
        Person exists = personRepo.findByEmail(person.getEmail());
        Person created;
        if (exists != null) {
            log.info("User with email {} exists.", person.getEmail());
            return null;
        } else {
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            person.setBalance(new BigDecimal(0));
            created = personRepo.save(person);
            log.info("Created {}.", created);
            return created;
        }
    }

    @Override
    @Transactional
    public Person update(Person person) throws Exception {
        Person toUpdate = personRepo.findByid(person.getId());
        Boolean changeDetected = false;
        // Check if we try to update a user different from the one
        // who is logged in now. We must only update the logged-in user.
        if (securityUtil.getLoggedInUser().getId() != person.getId()) {
            throw new Exception("Data Integrity Exception.");
        } else {
            // If the change of name is detected - update the name.
            if (!person.getName().equals("") && !person.getName().equals(toUpdate.getName())) {
                toUpdate.setName(person.getName());
                changeDetected = true;
            }
            // If the change of e-mail is detected - update the e-mail.
            if (!person.getEmail().equals("") && !person.getEmail().equals(toUpdate.getEmail())) {
                toUpdate.setEmail(person.getEmail());
                changeDetected = true;
            }
            // If the change of password is detected - update the password.
            if (!person.getPassword().equals("") && !person.getPassword().equals(toUpdate.getPassword())) {
                toUpdate.setPassword(passwordEncoder.encode(person.getPassword()));
                changeDetected = true;
            }
        }
        if (changeDetected) {
            log.info("Updated {}", toUpdate);
            return personRepo.save(toUpdate);
        } else {
            return toUpdate;
        }
    }

    @Override
    public Person findById(Integer id) throws NotFoundException {
        Person person = personRepo.findByid(id);
        if (person == null) {
            throw new NotFoundException(String.format("Entity with id %s does not exist.", id));
        } else {
            return person;
        }
    }

    @Override
    public Person increaseBalance(Person person) throws Exception {
        Person toUpdate = personRepo.findByid(person.getId());
        // Check if we try to update the balance of the user different from 
        // the one who is logged in now. We must only update the logged-in user.
        if (securityUtil.getLoggedInUser().getId() != person.getId()) {
            throw new Exception("Data Integrity Exception.");
        }
        // If amount is zero or negative
        else if (person.getBalance() == null || person.getBalance().compareTo(new BigDecimal(0)) <= 0) {
            throw new Exception("Amount cannot be null, zero or negative.");
        } else {
            toUpdate.setBalance(toUpdate.getBalance().add(person.getBalance()));
            personRepo.save(toUpdate);
            log.info("Balance of userID {} was increased by {}.", toUpdate.getId(), person.getBalance());
        }
        return toUpdate;
    }

    @Override
    public Person decreaseBalance(Person person) throws Exception {
        Person toUpdate = personRepo.findByid(person.getId());
        // Check if we try to update the balance of the user different from 
        // the one who is logged in now. We must only update the logged-in user.
        if (securityUtil.getLoggedInUser().getId() != person.getId()) {
            throw new Exception("Data Integrity Exception.");
        }
        // If amount is zero or negative
        else if (person.getBalance() == null || person.getBalance().compareTo(new BigDecimal(0)) <= 0) {
            throw new Exception("Amount cannot be null, zero or negative.");
        } 
        else if (toUpdate.getBalance().compareTo(person.getBalance()) < 0) {
            throw new Exception("Not enough balance for payout. Decrease the payout amount.");
        } 
        else {
            toUpdate.setBalance(toUpdate.getBalance().subtract(person.getBalance()));
            personRepo.save(toUpdate);
            log.info("Balance of userID {} was decreased by {}.", toUpdate.getId(), person.getBalance());
        }
        return toUpdate;
    }

    @Override
    public Person delete(Integer id) throws NotFoundException {
        Person prs = personRepo.findByid(id);
		if (prs == null) {
			throw new NotFoundException(String.format("Entity with id %s does not exist.", id));
		}
		 else {
			personRepo.deleteByPersonID(id);
			log.info("Deleted {}.", prs);
			return prs;
		}
    }
}
