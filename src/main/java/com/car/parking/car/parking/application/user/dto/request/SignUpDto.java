package com.car.parking.car.parking.application.user.dto.request;

import com.car.parking.car.parking.entity.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class SignUpDto {

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    private Set<Role> role;

    @NotBlank
    private String password;
}
