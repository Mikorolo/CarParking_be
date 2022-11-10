package com.car.parking.car.parking.entity;

import com.car.parking.car.parking.application.parkingspot.enums.Status;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ParkingSpot")
public class ParkingSpotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String sector;
    @Column
    private Integer floor;

    @Enumerated
    @Column(name = "status")
    private Status status;
}
