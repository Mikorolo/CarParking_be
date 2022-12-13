package com.car.parking.car.parking.application.user.services;

import com.car.parking.car.parking.application.user.dto.UpdateUserDto;
import com.car.parking.car.parking.application.user.repository.UserRepository;
import com.car.parking.car.parking.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    @Autowired private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userRes = userRepo.findByEmail(email);
        if(userRes.isEmpty())
            throw new UsernameNotFoundException("Could not findUser with email = " + email);
        UserEntity user = userRes.get();
        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public void handleUpdatePhoto(MultipartFile multipartFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepo.findByEmail(authentication.getPrincipal().toString()).get();
        try {
            userEntity.setPhoto(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userRepo.save(userEntity);
    }

    public byte[] getUserPhoto(String userEmail) {
        return userRepo.findByEmail(userEmail).get().getPhoto();
    }
}
