package com.car.parking.car.parking.application.user.exception;

public class UserExistsException extends RuntimeException{
    public UserExistsException() {
        super("Użytkownik o takim adresie e-mail już istnieje");
    }
}
