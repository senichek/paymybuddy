package com.open.paymybuddy.controllers;

import com.open.paymybuddy.services.MoneyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TransactionController {

    @Autowired
    MoneyTransactionService moneyTransactionService;

    /* @PostMapping(value = "/transaction/{senderID}/{receiverEmail}/{amount}")
    public ResponseEntity<MoneyTransaction> create(
        @PathVariable("senderID") Integer id,
        @PathVariable("receiverEmail") String receiverEmail, 
        @PathVariable("amount") BigDecimal amount) throws Exception {
        return new ResponseEntity<>(moneyTransactionService.create(id, receiverEmail, amount), HttpStatus.OK);
    } */
   
}
