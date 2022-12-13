package com.car.parking.car.parking.entity;

import com.car.parking.car.parking.application.user.dto.UpdateUserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;


@Builder
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Email
    private String email;

    private String password;

    private String name;

    private String lastname;

    private String carBrand;

    private Boolean isReserved = false;

    private Long spotNumber;

    @NotEmpty
    private String plateNumber;

    @Lob
    @JsonIgnore
    private byte[] photo;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public static UserEntity of(UpdateUserDto updateUserDto, UserEntity userEntity) {
        userEntity.setUsername(updateUserDto.getUsername());
        userEntity.setName(updateUserDto.getName());
        userEntity.setLastname(updateUserDto.getLastname());
        userEntity.setCarBrand(updateUserDto.getCarBrand());

        return userEntity;
    }
}