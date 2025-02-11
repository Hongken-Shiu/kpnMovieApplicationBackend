package com.kpn.movieservice.service;

import com.kpn.movieservice.domain.FavouriteMovies;
import com.kpn.movieservice.domain.User;
import com.kpn.movieservice.exception.MovieAlreadyExistsException;
import com.kpn.movieservice.exception.MovieNotFoundException;
import com.kpn.movieservice.exception.UserAlreadyExistsException;
import com.kpn.movieservice.exception.UserNotFoundException;
import com.kpn.movieservice.proxy.UserProxy;
import com.kpn.movieservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class IMovieServiceImpl implements IMovieService {

	private final MovieRepository movieRepository;
	private final UserProxy userProxy;

	@Autowired
	public IMovieServiceImpl(MovieRepository movieRepository, UserProxy userProxy) {
		this.movieRepository = movieRepository;
		this.userProxy = userProxy;
	}

	@Override
	public User registerUser(User user) throws UserAlreadyExistsException {
		Optional<User> registeruser = movieRepository.findById(user.getEmail());
		if (registeruser.isPresent()) {
			throw new UserAlreadyExistsException();
		} else {
			// Register a new user
			User addedUser = movieRepository.save(user);
			userProxy.saveUser(addedUser);
			return addedUser;
		}
	}

	@Override
	public FavouriteMovies registerFavourite(FavouriteMovies favouriteMovie, String email) throws MovieAlreadyExistsException, UserNotFoundException {
		// Check if the user exists
		Optional<User> optionalUser = movieRepository.findById(email);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// Initialize userFavouriteMovies
			List<FavouriteMovies> userFavouriteMovies = user.getFavouriteMovies();
			if (userFavouriteMovies == null) {
				userFavouriteMovies = new ArrayList<>();
			}

			boolean movieExists = userFavouriteMovies.stream()
					.anyMatch(movie -> movie.getMovieId().equals(favouriteMovie.getMovieId()));
			if (movieExists) {
				System.out.println("Movie already exists in the list of favorite movies.");
			} else {
				userFavouriteMovies.add(favouriteMovie);
				user.setFavouriteMovies(userFavouriteMovies);
				movieRepository.save(user);
				System.out.println("Movie added to the list of favorite movies.");
			}
			return favouriteMovie; // Return the added favourite movie
		} else {
			throw new UserNotFoundException();
		}
	}


	@Override
	public boolean deleteMovieFromFavourites(String movieId, String email) throws MovieNotFoundException {
		Optional<User> optionalUser = movieRepository.findById(email);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<FavouriteMovies> favoriteMovies = user.getFavouriteMovies();
			// Check if the list contains a FavouriteMovies object with the given movieId
			boolean found = favoriteMovies.stream().anyMatch(movie -> movie.getMovieId().equals(movieId));
			if (found) {
				// Remove the movie ID from the list of favorite movies
				favoriteMovies.removeIf(movie -> movie.getMovieId().equals(movieId));
				// Update the user entity with the modified list
				user.setFavouriteMovies(favoriteMovies);
				// Save the updated user entity to the database
				movieRepository.save(user);
				return true; // Return true indicating successful deletion
			} else {
				throw new MovieNotFoundException();
			}
		} else {
			throw new MovieNotFoundException();
		}
	}

//	@Override
//	public List<User> getAllUser(String email) throws UserNotFoundException {
//		List<User> userList = movieRepository.findAll();
//		if (userList.isEmpty()) {
//			throw new UserNotFoundException();
//		} else {
//			return userList;
//		}
//	}

	@Override
	public List<FavouriteMovies> getAllFavouriteMovies(String userId) throws UserNotFoundException {
		Optional<User> optionalUser = movieRepository.findById(userId);
		if (optionalUser.isPresent()) {
			return optionalUser.get().getFavouriteMovies();
		} else {
			throw new UserNotFoundException();
		}
	}

	@Override
	public FavouriteMovies getFavouriteMovieByMovieId(String movieId) throws MovieNotFoundException {
		Optional<User> userOptional = movieRepository.findByFavouriteMoviesMovieId(movieId);
		if (userOptional.isPresent()) {
			List<FavouriteMovies> favouriteMovies = userOptional.get().getFavouriteMovies();
			for (FavouriteMovies movie : favouriteMovies) {
				if (movie.getMovieId().equals(movieId)) {
					return movie;
				}
			}
			throw new MovieNotFoundException();
		} else {
			throw new MovieNotFoundException();
		}
	}

}