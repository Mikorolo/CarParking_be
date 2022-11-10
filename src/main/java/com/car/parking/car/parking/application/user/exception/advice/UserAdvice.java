package com.car.parking.car.parking.application.user.exception.advice;

import com.car.parking.car.parking.application.user.exception.LoginException;
import com.car.parking.car.parking.application.user.exception.UserDetailsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class UserAdvice {
    @ResponseBody
    @ExceptionHandler(value = {
            LoginException.class,
            UserDetailsException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String conflictAdvice(RuntimeException ex) {
        log.warn(String.format("Error:'%s'", ex.getMessage()));
        return ex.getMessage();
    }
}
