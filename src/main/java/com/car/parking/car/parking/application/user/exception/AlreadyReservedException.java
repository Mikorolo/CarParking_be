package com.car.parking.car.parking.application.user.exception;

public class AlreadyReservedException extends RuntimeException{
    public AlreadyReservedException() {
        super("Masz już zarezerwowane miejsce");
    }
}
