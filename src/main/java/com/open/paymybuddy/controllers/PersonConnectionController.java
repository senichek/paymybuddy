package com.open.paymybuddy.controllers;

import java.util.List;

import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.services.PersonConnectionsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class PersonConnectionController {

    @Autowired
    PersonConnectionsService personConnectionsService;

    /* @PostMapping(value = "/transaction/{senderID}/{receiverEmail}/{amount}")
    public ResponseEntity<MoneyTransaction> create(
        @PathVariable("senderID") Integer id,
        @PathVariable("receiverEmail") String receiverEmail, 
        @PathVariable("amount") BigDecimal amount) throws Exception {
        return new ResponseEntity<>(moneyTransactionService.create(id, receiverEmail, amount), HttpStatus.OK); 
    } */

    @GetMapping(value="/connection/{ownerID}")
    public ResponseEntity<List<PersonConnection>> getAllByOwnerID(@PathVariable("ownerID") Integer id) {
        return new ResponseEntity<>(personConnectionsService.getAllByOwnerID(id), HttpStatus.OK);
    }
}
