package com.kpn.movieservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserAlreadyExistsException extends Exception{

}
