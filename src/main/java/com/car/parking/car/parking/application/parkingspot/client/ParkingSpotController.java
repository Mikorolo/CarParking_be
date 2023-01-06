package com.car.parking.car.parking.application.parkingspot.client;

import com.car.parking.car.parking.application.parkingspot.dto.MakeReservationDTO;
import com.car.parking.car.parking.application.parkingspot.dto.OccupySpotDto;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @PostMapping("/testExternal")
    public String testExternal() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");

        headers.setAll(map);

        Map req_payload = new HashMap<>();
        req_payload.put("floor", 1);
        req_payload.put("sector", "A1");

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        String url = "http://192.168.137.153:8080/addReservation";

        new RestTemplate().postForEntity(url, request, String.class);

        return "Done";
    }

    @GetMapping("/test")
    public String callGet() {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> exchange = rest.exchange(
                "http://192.168.137.153:8080/led/light",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class);
        return exchange.getBody();
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

    @PatchMapping("/MakeAutomaticReservation")
    public ParkingSpotEntity makeAutomaticReservation() {
        return parkingSpotService.makeAutomaticReservation();
    }

    @PatchMapping("/OccupySpot")
    public ParkingSpotEntity occupySpot(@RequestParam Long id) {
        return parkingSpotService.occupySpot(id);
    }

    @PatchMapping("/CancelReservation")
    public ParkingSpotEntity cancelReservation(@RequestBody MakeReservationDTO makeReservationDTO) {
        return parkingSpotService.cancelReservation(makeReservationDTO);
    }

    @DeleteMapping("/DeleteParkingSpot")
    public void deleteParkingSpot(@RequestBody Long id) {
        parkingSpotService.deleteParkingSpot(id);
    }
}
