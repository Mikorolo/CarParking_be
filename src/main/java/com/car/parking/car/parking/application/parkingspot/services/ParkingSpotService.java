package com.car.parking.car.parking.application.parkingspot.services;


import com.car.parking.car.parking.application.parkingspot.dto.*;
import com.car.parking.car.parking.application.parkingspot.enums.Status;
import com.car.parking.car.parking.application.parkingspot.repository.ParkingSpotRepositoryPort;
import com.car.parking.car.parking.application.user.dto.UpdateUserDto;
import com.car.parking.car.parking.application.user.exception.AlreadyReservedException;
import com.car.parking.car.parking.application.user.exception.CancelReservationException;
import com.car.parking.car.parking.application.user.repository.UserRepository;
import com.car.parking.car.parking.entity.ParkingSpotEntity;
import com.car.parking.car.parking.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class ParkingSpotService {
    private final ParkingSpotRepositoryPort parkingSpotRepositoryPort;
    @Autowired
    private UserRepository userRepo;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepo.findByEmail(authentication.getPrincipal().toString()).get();
        if(!userEntity.getIsReserved()){
            parkingSpotEntity.setStatus(Status.RESERVED);
            parkingSpotEntity.setReservationDate(LocalDateTime.now().plusMinutes(1));
            parkingSpotEntity.setUserEmail(userEntity.getEmail());
            userEntity.setIsReserved(true);
            userEntity.setSpotNumber(makeReservationDTO.getId());
            userRepo.save(userEntity);
            RestTemplate rest = new RestTemplate();
            rest.exchange(
                    "http://192.168.137.153:8080/led/reservation/on",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    String.class);
            return parkingSpotRepositoryPort.save(parkingSpotEntity);
        }
        else {
            throw new AlreadyReservedException();
        }
    }

    public ParkingSpotEntity makeAutomaticReservation() {
        Random random = new Random();
        List<ParkingSpotEntity> status = parkingSpotRepositoryPort.findAllByStatus(Status.FREE);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepo.findByEmail(authentication.getPrincipal().toString()).get();
        if(!userEntity.getIsReserved()){
            ParkingSpotEntity spot = status.stream().findAny().orElseThrow(() -> new RuntimeException("NIe ma wolnych miejsc"));
            spot.setStatus(Status.RESERVED);
            spot.setReservationDate(LocalDateTime.now().plusMinutes(1));
            spot.setUserEmail(userEntity.getEmail());
            userEntity.setIsReserved(true);
            userEntity.setSpotNumber(spot.getId());
            userRepo.save(userEntity);
            RestTemplate rest = new RestTemplate();
            rest.exchange(
                    "http://192.168.137.153:8080/led/reservation/on",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    String.class);
            return parkingSpotRepositoryPort.save(spot);
        }
        else {
            throw new AlreadyReservedException();
        }
    }

    public ParkingSpotEntity occupySpot(Long id) {
        ParkingSpotEntity parkingSpotEntity = parkingSpotRepositoryPort.findParkingSpotEntityById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepo.findByEmail(authentication.getPrincipal().toString()).get();
        parkingSpotEntity.setStatus(Status.OCCUPIED);
        RestTemplate rest = new RestTemplate();
        rest.exchange(
                "http://192.168.137.153:8080/led/occupy/on",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class);
        RestTemplate rest1 = new RestTemplate();
        rest1.exchange(
                "http://192.168.137.153:8080/led/reservation/off",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class);
        return parkingSpotRepositoryPort.save(parkingSpotEntity);
    }

    public ParkingSpotEntity cancelReservation(MakeReservationDTO makeReservationDTO) {
        ParkingSpotEntity parkingSpotEntity = parkingSpotRepositoryPort.findParkingSpotEntityById((makeReservationDTO.getId()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepo.findByEmail(authentication.getPrincipal().toString()).get();
        if(userEntity.getSpotNumber() == makeReservationDTO.getId()) {
            userEntity.setIsReserved(false);
            userEntity.setSpotNumber(null);
            userRepo.save(userEntity);
            parkingSpotEntity.setStatus(Status.FREE);
            RestTemplate rest = new RestTemplate();
            rest.exchange(
                    "http://192.168.137.153:8080/led/reservation/off",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    String.class);
            RestTemplate rest1 = new RestTemplate();
            rest1.exchange(
                    "http://192.168.137.153:8080/led/occupy/off",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    String.class);
            return  parkingSpotRepositoryPort.save(parkingSpotEntity);
        }
        else {
            throw new CancelReservationException();
        }
    }

    @Scheduled(cron = "0/10 * * * * *")
    public void cancelScheduled() {
        parkingSpotRepositoryPort.findAllByStatus(Status.RESERVED).forEach(parkingSpotEntity -> {
            if(parkingSpotEntity.getReservationDate().isBefore(LocalDateTime.now())) {
                parkingSpotEntity.setStatus(Status.FREE);
                UserEntity userEntity = userRepo.findByEmail(parkingSpotEntity.getUserEmail()).get();
                userEntity.setSpotNumber(null);
                userEntity.setIsReserved(false);
                userRepo.save(userEntity);
                parkingSpotEntity.setReservationDate(null);
                RestTemplate rest = new RestTemplate();
                rest.exchange(
                        "http://192.168.137.153:8080/led/reservation/off",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        String.class);
                parkingSpotRepositoryPort.save(parkingSpotEntity);
            }
        });
        System.out.println("Sprawdzanie");
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
