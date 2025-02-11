package com.kpn.movieservice.controller;

import com.kpn.movieservice.domain.User;
import com.kpn.movieservice.exception.UserAlreadyExistsException;
import com.kpn.movieservice.service.IMovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class UserController {
	private final IMovieService iMovieService;

	public UserController(IMovieService iMovieService) {
		this.iMovieService = iMovieService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		// Register a new user and save to db, return 201 status if user is saved else 500 status
		ResponseEntity responseEntity;
		// Register a new user and save to db,
		try{
			User register=iMovieService.registerUser(user);
			responseEntity=new ResponseEntity(register, HttpStatus.CREATED);
			return responseEntity;
		}catch(UserAlreadyExistsException e){
			responseEntity=new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
			System.out.println(responseEntity);
			return responseEntity;
		}
//		return responseEntity;
	}
}
