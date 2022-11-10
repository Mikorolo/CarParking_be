package com.car.parking.car.parking.application.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class UpdateUserDto {
    String username;

    String name;

    String lastname;

    String carBrand;
}
