package com.kpn.userauthentication.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User Already Present")
public class UserAlreadyExistException extends Exception{
}
