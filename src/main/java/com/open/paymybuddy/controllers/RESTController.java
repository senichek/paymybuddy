package com.open.paymybuddy.controllers;

import java.util.List;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.services.MoneyTransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RESTController {

    @Autowired
    MoneyTransactionService moneyTransactionService;

    @GetMapping(value="/rest/transfer")
    public ResponseEntity<List<MoneyTransaction>> getTransactions() throws Exception {
        List<MoneyTransaction> transactions = moneyTransactionService.getAllForLoggedIn("james@gmail.com");
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
