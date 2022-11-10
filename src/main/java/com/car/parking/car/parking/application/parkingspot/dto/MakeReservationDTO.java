package com.car.parking.car.parking.application.parkingspot.dto;

import com.car.parking.car.parking.application.parkingspot.enums.Status;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MakeReservationDTO {
    Long id;
    Status status;
    //Integer floor;
}
