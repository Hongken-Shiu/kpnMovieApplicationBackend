package com.kpn.userauthentication.controller;

import com.kpn.userauthentication.domain.User;
import com.kpn.userauthentication.exception.InvalidCredentialsException;
import com.kpn.userauthentication.exception.UserAlreadyExistException;
import com.kpn.userauthentication.security.SecurityTokenGenerator;
import com.kpn.userauthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v3")
public class UserController {

	private final UserService userService;
	private final SecurityTokenGenerator securityTokenGenerator;

	@Autowired
	public UserController(UserService userService, SecurityTokenGenerator securityTokenGenerator) {
		this.userService = userService;
		this.securityTokenGenerator = securityTokenGenerator;
	}

	@PostMapping("/register")
	public ResponseEntity<?> saveUser(@RequestBody User user) {
		// Write the logic to save a user,
		// return 201 status if user is saved else 500 status
		try {
			User savedUser = userService.saveUser(user);
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (UserAlreadyExistException exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
		}
	}


	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {
		// Generate the token on login,
		// return 200 status if user is saved else 500 status

		try {
			User loggedInUser = userService.getUserByUserIdAndPassword(user.getEmail(), user.getPassword());
			String token = securityTokenGenerator.createToken(loggedInUser).toString();
			Map<String,String> securityToken= new HashMap<>();
			securityToken.put("token",token);
			return new ResponseEntity<>(securityToken, HttpStatus.OK);
		} catch (InvalidCredentialsException exception) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
}



