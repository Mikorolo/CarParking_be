package com.car.parking.car.parking.application.user.repository;

import com.car.parking.car.parking.entity.Role;
import com.car.parking.car.parking.entity.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RolesEnum name);
}
