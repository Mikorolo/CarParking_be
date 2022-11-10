package com.car.parking.car.parking.application.parkingspot.services;


import com.car.parking.car.parking.application.parkingspot.dto.FindByFloorAndSectorDTO;
import com.car.parking.car.parking.application.parkingspot.dto.MakeReservationDTO;
import com.car.parking.car.parking.application.parkingspot.dto.ParkingSpotDTO;
import com.car.parking.car.parking.application.parkingspot.dto.UpdateParkingSpotDTO;
import com.car.parking.car.parking.application.parkingspot.enums.Status;
import com.car.parking.car.parking.application.parkingspot.repository.ParkingSpotRepositoryPort;
import com.car.parking.car.parking.application.user.dto.UpdateUserDto;
import com.car.parking.car.parking.entity.ParkingSpotEntity;
import com.car.parking.car.parking.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@AllArgsConstructor
@Service
public class ParkingSpotService {
    private final ParkingSpotRepositoryPort parkingSpotRepositoryPort;

    public List<ParkingSpotEntity> findParkingSpotByFloor(Integer floor) {
        return parkingSpotRepositoryPort.findParkingSpotEntityByFloor(floor);
    }

    public List<ParkingSpotEntity> findParkingSpotByFloorAndSector(Integer floor, String sector) {
        return parkingSpotRepositoryPort.findParkingSpotEntityByFloorAndSector(floor, sector);
    }

    public ParkingSpotEntity addParkingSpot(ParkingSpotDTO parkingSpotDTO) {
        ParkingSpotEntity parkingSpotEntity = ParkingSpotEntity
                .builder()
                .floor(parkingSpotDTO
                        .getFloor())
                .sector(parkingSpotDTO
                        .getSector())
                .build();
        parkingSpotEntity.setStatus(Status.FREE);

        return parkingSpotRepositoryPort.save(parkingSpotEntity);
    }

    public ParkingSpotEntity makeReservation(MakeReservationDTO makeReservationDTO) {
        ParkingSpotEntity parkingSpotEntity = parkingSpotRepositoryPort.findParkingSpotEntityById(makeReservationDTO.getId());
        parkingSpotEntity.setStatus(Status.RESERVED);
        return parkingSpotRepositoryPort.save(parkingSpotEntity);
    }

    public ParkingSpotEntity updateParkingSpot(UpdateParkingSpotDTO updateParkingSpotDTO) {
        ParkingSpotEntity parkingSpotEntity = ParkingSpotEntity
                .builder()
                .id(updateParkingSpotDTO
                        .getId())
                .floor(updateParkingSpotDTO
                        .getFloor())
                .build();
        parkingSpotEntity.setFloor(updateParkingSpotDTO.getFloor());
        return parkingSpotRepositoryPort.save(parkingSpotEntity);
    }

    @Transactional
    public void deleteParkingSpot(Long id) {
        parkingSpotRepositoryPort.deleteParkingSpotEntityById(id);
    }
}
