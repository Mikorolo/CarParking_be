package com.car.parking.car.parking.application.user.exception;

public class PlateNumberExistsException extends RuntimeException{
    public PlateNumberExistsException() {
        super("Użytkownik o podanym numerze rejestracyjnym istnieje");
    }
}
