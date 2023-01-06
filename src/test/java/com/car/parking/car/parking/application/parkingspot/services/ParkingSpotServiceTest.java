package com.car.parking.car.parking.application.parkingspot.services;

import com.car.parking.car.parking.application.parkingspot.enums.Status;
import com.car.parking.car.parking.application.parkingspot.repository.ParkingSpotRepositoryPort;
import com.car.parking.car.parking.application.user.client.AuthController;
import com.car.parking.car.parking.application.user.repository.UserRepository;
import com.car.parking.car.parking.entity.ParkingSpotEntity;
import com.car.parking.car.parking.entity.UserEntity;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingSpotServiceTest {

    @Autowired
    ParkingSpotService parkingSpotService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ParkingSpotRepositoryPort parkingSpotRepositoryPort;

    @Autowired
    AuthController authController;

    @Test
    void findParkingSpotByFloor() {
        //given
        Integer floor = 1;
        UserEntity newUser = new UserEntity("a@a.com", "asdf", "TK88327");
        authController.registerHandler(newUser);
        parkingSpotRepositoryPort.deleteAll();
        ParkingSpotEntity newSpot = new ParkingSpotEntity(1, "A1", Status.FREE);
        ParkingSpotEntity newSpot1 = new ParkingSpotEntity(1, "B1", Status.FREE);
        List<ParkingSpotEntity> expectedSpots = Arrays.asList(newSpot, newSpot1);
        parkingSpotRepositoryPort.saveAll(expectedSpots);
        //when
        List<ParkingSpotEntity> spotByFloor = parkingSpotService.findParkingSpotByFloor(floor);
        //then
        Assertions.assertEquals(expectedSpots.size(), spotByFloor.size());
        Assertions.assertEquals(expectedSpots.get(1).getId(), spotByFloor.get(1).getId());
    }

    @Test
    void findParkingSpotByFloorAndSector() {
    }

    @Test
    void addParkingSpot() {
    }

    @Test
    void makeReservation() {
    }

    @Test
    void makeAutomaticReservation() {
    }

    @Test
    void occupySpot() {
    }

    @Test
    void cancelReservation() {
    }

    @Test
    void updateParkingSpot() {
    }

    @Test
    void deleteParkingSpot() {
    }
}