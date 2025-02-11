package com.kpn.userauthentication.service;

import com.kpn.userauthentication.domain.User;
import com.kpn.userauthentication.exception.InvalidCredentialsException;
import com.kpn.userauthentication.exception.UserAlreadyExistException;

import java.util.List;

public interface UserService {
	User saveUser(User user) throws UserAlreadyExistException;
	User getUserByUserIdAndPassword(String userId, String password) throws InvalidCredentialsException;
	boolean deleteUser(String email) throws InvalidCredentialsException;
	public List<User> getAllUser() throws InvalidCredentialsException;
}
