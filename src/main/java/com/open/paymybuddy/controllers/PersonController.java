package com.open.paymybuddy.controllers;

import java.util.List;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.services.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping(value = "/person/all")
    public ResponseEntity<List<Person>> getAll() {
        return new ResponseEntity<>(personService.getAll(), HttpStatus.OK);
    }
    
}
