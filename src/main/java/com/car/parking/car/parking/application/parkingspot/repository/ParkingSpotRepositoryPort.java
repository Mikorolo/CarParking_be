package com.car.parking.car.parking.application.parkingspot.repository;

import com.car.parking.car.parking.application.parkingspot.enums.Status;
import com.car.parking.car.parking.entity.ParkingSpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpotRepositoryPort extends JpaRepository<ParkingSpotEntity, Long> {
    List<ParkingSpotEntity> findParkingSpotEntityByFloor(Integer floor);

    List<ParkingSpotEntity> findAllByStatus(Status status);
    List<ParkingSpotEntity> findParkingSpotEntityByFloorAndSector(Integer floor, String sector);
    void deleteParkingSpotEntityById(Long id);

    ParkingSpotEntity findParkingSpotEntityById(Long id);
}
