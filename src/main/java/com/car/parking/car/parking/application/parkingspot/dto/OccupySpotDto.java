package com.car.parking.car.parking.application.parkingspot.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OccupySpotDto {
    Long id;
    Integer floor;
    String sector;
}
