package com.open.paymybuddy.controllers;

import java.math.BigDecimal;
import java.util.List;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.services.MoneyTransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class TransactionController {

    @Autowired
    MoneyTransactionService moneyTransactionService;

    @PostMapping(value = "/transaction/{senderID}/{receiverEmail}/{amount}")
    public ResponseEntity<MoneyTransaction> create(
        @PathVariable("senderID") Integer id,
        @PathVariable("receiverEmail") String receiverEmail, 
        @PathVariable("amount") BigDecimal amount) throws Exception {
        return new ResponseEntity<>(moneyTransactionService.create(id, receiverEmail, amount), HttpStatus.OK);
    }

   /*  @GetMapping(value="/transaction/all")
    public ResponseEntity<List<MoneyTransaction>> getAll() {
        return new ResponseEntity<>(moneyTransactionService.getAll(), HttpStatus.OK);
    } */
}
