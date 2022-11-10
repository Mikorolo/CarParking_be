package com.car.parking.car.parking.application.user.exception;

public class LoginException extends RuntimeException {
    public LoginException() {
        super("Złe dane logowania");
    }
}
