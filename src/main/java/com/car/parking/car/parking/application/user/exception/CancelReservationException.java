package com.car.parking.car.parking.application.user.exception;

public class CancelReservationException extends RuntimeException {
    public CancelReservationException() {
        super("Nie możesz anulować rezerwacji miejsca innego niż zarezerwowano wcześniej");
    }
}
