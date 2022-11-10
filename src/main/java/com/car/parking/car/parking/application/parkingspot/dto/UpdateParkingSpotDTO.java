package com.car.parking.car.parking.application.parkingspot.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateParkingSpotDTO {
    Long id;
    Integer floor;
}
