package com.car.parking.car.parking.entity;

import com.car.parking.car.parking.entity.enums.RolesEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    @Enumerated
    private RolesEnum name;
}
