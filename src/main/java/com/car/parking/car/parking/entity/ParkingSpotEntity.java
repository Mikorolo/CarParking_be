package com.car.parking.car.parking.entity;

import com.car.parking.car.parking.application.parkingspot.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column
    private String userEmail;

    @Column
    private LocalDateTime reservationDate;

    public ParkingSpotEntity(Integer floor, String sector, Status status) {
        this.floor = floor;
        this.sector = sector;
        this.status = status;
    }
}