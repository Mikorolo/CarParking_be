package com.car.parking.car.parking.application.user.exception;

public class UserDetailsException extends RuntimeException {
    public UserDetailsException() {
        super("Brak uprawnień");
    }
}
