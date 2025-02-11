package com.kpn.movieservice.service;

import com.kpn.movieservice.domain.FavouriteMovies;
import com.kpn.movieservice.domain.User;
import com.kpn.movieservice.exception.MovieAlreadyExistsException;
import com.kpn.movieservice.exception.MovieNotFoundException;
import com.kpn.movieservice.exception.UserAlreadyExistsException;
import com.kpn.movieservice.exception.UserNotFoundException;
import com.kpn.movieservice.proxy.UserProxy;
import com.kpn.movieservice.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserProxy userProxy;

    @InjectMocks
    private IMovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() throws UserAlreadyExistsException {
        User user = new User("test@example.com", "testUser", "password", "123456789", "male", "profile.jpg", new ArrayList<>());
        when(movieRepository.findById("test@example.com")).thenReturn(Optional.empty());
        when(movieRepository.save(any())).thenReturn(user);
        when(userProxy.saveUser(any())).thenReturn(null); // Mocking the behavior of saveUser method

        User registeredUser = movieService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("test@example.com", registeredUser.getEmail());
        assertEquals("testUser", registeredUser.getUserName());
    }


    @Test
    void testRegisterUser_UserAlreadyExistsException() {
        User existingUser = new User("existing@example.com", "existingUser", "password", "123456789", "male", "profile.jpg", new ArrayList<>());
        when(movieRepository.findById("existing@example.com")).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistsException.class, () -> movieService.registerUser(existingUser));
    }

    @Test
    void testRegisterFavourite_Success() throws MovieAlreadyExistsException, UserNotFoundException {
        // Arrange
        String email = "test@example.com";
        User user = new User(email, "testUser", "password", "123456789", "male", "profile.jpg", new ArrayList<>());
        FavouriteMovies favouriteMovie = new FavouriteMovies("123", "Movie Title", email, Arrays.asList(1, 2, 3));
        when(movieRepository.findById(email)).thenReturn(Optional.of(user));
        when(movieRepository.save(any())).thenReturn(user);

        // Act
        FavouriteMovies result = movieService.registerFavourite(favouriteMovie, email);

        // Assert
        assertNotNull(result);
        assertEquals(favouriteMovie, result);
    }



    @Test
    void testRegisterFavourite_UserNotFoundException() {
        FavouriteMovies favouriteMovie = new FavouriteMovies("123", "Movie Title", "test@example.com", Arrays.asList(1, 2, 3));
        when(movieRepository.findById("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> movieService.registerFavourite(favouriteMovie, "test@example.com"));
    }


    @Test
    void testDeleteMovieFromFavourites_Success() throws MovieNotFoundException {
        User user = new User("test@example.com", "testUser", "password", "123456789", "male", "profile.jpg", new ArrayList<>());
        user.getFavouriteMovies().add(new FavouriteMovies("123", "Movie Title", "test@example.com", Arrays.asList(1, 2, 3)));
        when(movieRepository.findById("test@example.com")).thenReturn(Optional.of(user));
        doNothing().when(movieRepository).deleteById("123");

        boolean result = movieService.deleteMovieFromFavourites("123", "test@example.com");

        assertTrue(result);
    }

    @Test
    void testDeleteMovieFromFavourites_MovieNotFoundException() {
        when(movieRepository.findById("test@example.com")).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovieFromFavourites("123", "test@example.com"));
    }

	@Test
	void testGetAllFavouriteMovies_Success() throws UserNotFoundException {
		// Arrange
		String userId = "test@example.com";
		List<FavouriteMovies> expectedMovies = new ArrayList<>();
		expectedMovies.add(new FavouriteMovies("123", "Movie Title 1", userId, Arrays.asList(1, 2, 3)));
		expectedMovies.add(new FavouriteMovies("456", "Movie Title 2", userId, Arrays.asList(1, 2, 3)));
		User user = new User(userId, "testUser", "password", "123456789", "male", "profile.jpg", expectedMovies);
		when(movieRepository.findById(userId)).thenReturn(Optional.of(user));

		// Act
		List<FavouriteMovies> result = movieService.getAllFavouriteMovies(userId);

		// Assert
		assertNotNull(result);
		assertEquals(expectedMovies.size(), result.size());
		assertEquals(expectedMovies.get(0), result.get(0));
		assertEquals(expectedMovies.get(1), result.get(1));
	}
	@Test
	void testGetAllFavouriteMovies_UserNotFoundException() {
		// Arrange
		String userId = "test@example.com";
		when(movieRepository.findById(userId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(UserNotFoundException.class, () -> movieService.getAllFavouriteMovies(userId));
	}
}
