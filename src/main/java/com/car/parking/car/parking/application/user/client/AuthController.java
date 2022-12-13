package com.car.parking.car.parking.application.user.client;

import com.car.parking.car.parking.application.user.dto.request.LoginDto;
import com.car.parking.car.parking.application.user.exception.LoginException;
import com.car.parking.car.parking.application.user.exception.PlateNumberExistsException;
import com.car.parking.car.parking.application.user.exception.UserExistsException;
import com.car.parking.car.parking.application.user.repository.UserRepository;
import com.car.parking.car.parking.configurations.security.JWTUtil;
import com.car.parking.car.parking.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody UserEntity user){
        String encodedPass = passwordEncoder.encode(user.getPassword());
        if(userRepo.existsByEmail(user.getEmail())) {
            throw new UserExistsException();
        }
        if(userRepo.existsUserEntityByPlateNumber(user.getPlateNumber())) {
            throw new PlateNumberExistsException();
        }
        user.setPassword(encodedPass);
        user.setPlateNumber(user.getPlateNumber());
        user = userRepo.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return Collections.singletonMap("jwt", token);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginDto body){
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getEmail());

            return Collections.singletonMap("jwt", token);
        }
        catch (AuthenticationException authExc){
            throw new LoginException();
        }
    }
}
