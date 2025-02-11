package com.kpn.movieservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class MovieAlreadyExistsException extends Exception{

}
