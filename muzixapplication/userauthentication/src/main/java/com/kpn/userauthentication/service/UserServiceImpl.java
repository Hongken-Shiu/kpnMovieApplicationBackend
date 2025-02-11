package com.kpn.userauthentication.service;

import com.kpn.userauthentication.domain.User;
import com.kpn.userauthentication.exception.InvalidCredentialsException;
import com.kpn.userauthentication.exception.UserAlreadyExistException;
import com.kpn.userauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

private final UserRepository userRepository;


@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Override
	public User saveUser(User user) throws UserAlreadyExistException {
		if (userRepository.findById(user.getEmail()).isPresent())
			throw new UserAlreadyExistException();
		else {
			return userRepository.save(user);
		}
	}
	@Override
	public User getUserByUserIdAndPassword(String email, String password) throws InvalidCredentialsException {
		User user = userRepository.findByEmailAndPassword(email, password);
		if (user == null) {
			throw new InvalidCredentialsException();

		} else {
			return user;
		}
	}

	@Override
	public boolean deleteUser(String email) throws InvalidCredentialsException {
		Optional<User> optionalUser = userRepository.findById(email);
		if(optionalUser.isPresent())
		{
			userRepository.deleteById(email);
			return true;
		}
		else {
			throw new InvalidCredentialsException();
		}
	}
	@Override
	public List<User> getAllUser() throws InvalidCredentialsException {
		List<User> userList=(List<User>) userRepository.findAll();
		if(userList.isEmpty()){
			throw new InvalidCredentialsException();
		}
		else {
			return userList;
		}
	}


}


