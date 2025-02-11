package com.kpn.movieservice.controller;
import com.kpn.movieservice.domain.FavouriteMovies;
import com.kpn.movieservice.domain.User;
import com.kpn.movieservice.exception.MovieAlreadyExistsException;
import com.kpn.movieservice.exception.MovieNotFoundException;
import com.kpn.movieservice.exception.UserNotFoundException;
import com.kpn.movieservice.service.IMovieService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class MovieController {
	private final IMovieService iMovieService;
	@Autowired
	public MovieController(IMovieService iMovieService) {
		this.iMovieService = iMovieService;
	}

	@PostMapping("/favourite")
	public ResponseEntity<?> registerFavourite(@RequestBody FavouriteMovies favouriteMovie, HttpServletRequest request) {
	try {
		System.out.println("favourite movie here: " + favouriteMovie);
		System.out.println("request here: " + request);
		String userId = getUserIdFromClaims(request);
		System.out.println("user id here: " + userId);

		FavouriteMovies registeredMovie = iMovieService.registerFavourite(favouriteMovie, userId);
		System.out.println("registered movie here: " + registeredMovie);

		return new ResponseEntity<>(registeredMovie, HttpStatus.CREATED);
	} catch (MovieAlreadyExistsException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	} catch (UserNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	} catch (Exception e) {
		e.printStackTrace(); // Print the stack trace for detailed error information
		System.out.println("Exception: " + e);
		return new ResponseEntity<>("Unexpected error occurred while registering favourite movie: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
	@DeleteMapping("/user/{email}/{movieId}")
	public ResponseEntity<String> deleteMovieFromFavourites(@PathVariable String email, @PathVariable String movieId) throws MovieNotFoundException {
		try {
			boolean deleted = iMovieService.deleteMovieFromFavourites(movieId, email);
			if (deleted) {
				return ResponseEntity.ok("Movie deleted successfully from favorites.");
			} else {
				return ResponseEntity.notFound().build(); // or ResponseEntity.ok("Movie not found in favorites.");
			}
		} catch (MovieNotFoundException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Movie already deleted from favourites: " + e.getMessage());
		}
	}


//	@GetMapping("/users")
//	public ResponseEntity<?> getAllUser(HttpServletRequest request)  {
//
//		try {
//			String userId = getUserIdFromClaims(request);
//			List<User> productList = iMovieService.getAllUser(userId);
//			return new ResponseEntity<>(productList, HttpStatus.OK);
//		} catch (UserNotFoundException exception) {
//			return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
//		}
//	}
	@GetMapping("/getFavourite")
	public ResponseEntity<List<FavouriteMovies>> getAllFavouriteMovies(HttpServletRequest request) {
		try {
			String userId = getUserIdFromClaims(request);
			List<FavouriteMovies> favouriteMovies = iMovieService.getAllFavouriteMovies(userId);
			return new ResponseEntity<>(favouriteMovies, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/favourite/{movieId}")
	public ResponseEntity<?> getFavouriteMovieByMovieId(@PathVariable String movieId) {
		try {
			FavouriteMovies movie = iMovieService.getFavouriteMovieByMovieId(movieId);
			return new ResponseEntity<>(movie, HttpStatus.OK);
		} catch (MovieNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}


	private String getUserIdFromClaims(HttpServletRequest request) {
		return request.getAttribute("userId").toString();
//		return request.getAttribute("userId");
	}


	}


