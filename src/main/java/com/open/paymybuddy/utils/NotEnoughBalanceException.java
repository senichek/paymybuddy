package com.open.paymybuddy.utils;

public class NotEnoughBalanceException extends Exception {
    public NotEnoughBalanceException(String message) {
        super(message);
    }
}
