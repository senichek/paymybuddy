package com.open.paymybuddy.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping(value = "/greet")
    public String Greetings() {
        return "PayMyBuddy app is running";
    }
}