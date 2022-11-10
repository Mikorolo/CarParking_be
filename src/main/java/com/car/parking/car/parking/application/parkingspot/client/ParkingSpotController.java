package com.car.parking.car.parking.application.parkingspot.client;

import com.car.parking.car.parking.application.parkingspot.dto.MakeReservationDTO;
import com.car.parking.car.parking.application.parkingspot.dto.ParkingSpotDTO;
import com.car.parking.car.parking.application.parkingspot.dto.UpdateParkingSpotDTO;
import com.car.parking.car.parking.application.parkingspot.repository.ParkingSpotRepositoryPort;
import com.car.parking.car.parking.application.parkingspot.services.ParkingSpotService;
import com.car.parking.car.parking.application.user.dto.UpdateUserDto;
import com.car.parking.car.parking.application.user.exception.UserDetailsException;
import com.car.parking.car.parking.application.user.repository.UserRepository;
import com.car.parking.car.parking.application.user.services.MyUserDetailsService;
import com.car.parking.car.parking.entity.ParkingSpotEntity;
import com.car.parking.car.parking.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class ParkingSpotController {
    private final ParkingSpotService parkingSpotService;
    private final ParkingSpotRepositoryPort parkingSpotRepositoryPort;

    private final MyUserDetailsService userService;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/GetParkingSpot")
    public List<ParkingSpotEntity> findParkingSpotByFloor(@RequestParam Integer floor) {
        return parkingSpotService.findParkingSpotByFloor(floor);
    }

    @GetMapping("/info")
    public UserEntity getUserDetails(){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).orElseThrow(() -> {throw new UserDetailsException();});
    }

    @PatchMapping("/updateUser")
    public void updateUsername(@RequestBody UpdateUserDto updateUserDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepo.findByEmail(authentication.getPrincipal().toString()).get();

        userRepo.save(UserEntity.of(updateUserDto, userEntity));
    }

    @PatchMapping("/updatePhoto")
    public void updatePhoto(@RequestBody MultipartFile multipartFile) {
        userService.handleUpdatePhoto(multipartFile);
    }

    @GetMapping(value = "/userPhoto/{userEmail}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getUserPhoto(@PathVariable String userEmail) {
        return userService.getUserPhoto(userEmail);
    }

    @GetMapping("/GetByFloorAndSector")
    public List<ParkingSpotEntity> findParkingSpotByFloorAndSector(@RequestParam Integer floor, @RequestParam String sector) {
        return parkingSpotService.findParkingSpotByFloorAndSector(floor, sector);
    }

    @GetMapping("/GetById")
    public Optional<ParkingSpotEntity> getById(@RequestParam Long id) {
        return parkingSpotRepositoryPort.findById(id);
    }

    @PostMapping("/AddParkingSpot")
    public ParkingSpotEntity addParkingSpot(@RequestBody ParkingSpotDTO parkingSpotDTO) {
        return parkingSpotService.addParkingSpot(parkingSpotDTO);
    }

    @PatchMapping("/UpdateParkingSpot")
    public ParkingSpotEntity updateParkingSpot(@RequestBody UpdateParkingSpotDTO updateParkingSpotDTO) {
        return parkingSpotService.updateParkingSpot(updateParkingSpotDTO);
    }

    @PatchMapping("/MakeReservation")
    public ParkingSpotEntity makeReservation(@RequestBody MakeReservationDTO makeReservationDTO) {
        return parkingSpotService.makeReservation(makeReservationDTO);
    }

    @DeleteMapping("/DeleteParkingSpot")
    public void deleteParkingSpot(@RequestBody Long id) {
        parkingSpotService.deleteParkingSpot(id);
    }
}
