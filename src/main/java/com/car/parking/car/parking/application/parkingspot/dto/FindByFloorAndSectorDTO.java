package com.car.parking.car.parking.application.parkingspot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class FindByFloorAndSectorDTO {
    Integer floor;
    String sector;
}
