package com.kpn.movieservice.controller;

import com.kpn.movieservice.controller.MovieController;
import com.kpn.movieservice.domain.FavouriteMovies;
import com.kpn.movieservice.domain.User;
import com.kpn.movieservice.exception.MovieAlreadyExistsException;
import com.kpn.movieservice.exception.MovieNotFoundException;
import com.kpn.movieservice.exception.UserNotFoundException;
import com.kpn.movieservice.service.IMovieService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    IMovieService movieService;

    @Mock
    HttpServletRequest request;

    @InjectMocks
    MovieController movieController;


    @Test
    void testDeleteMovieFromFavourites_Success() throws MovieNotFoundException {
        // Arrange
        String email = "test@example.com";
        String movieId = "123";
        when(movieService.deleteMovieFromFavourites(movieId, email)).thenReturn(true);

        // Act
        ResponseEntity<String> responseEntity = movieController.deleteMovieFromFavourites(email, movieId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Movie deleted successfully from favorites.", responseEntity.getBody());
    }

    @Test
    void testDeleteMovieFromFavourites_MovieNotFound() throws MovieNotFoundException {
        String email = "test@example.com";
        String movieId = "123";
        when(movieService.deleteMovieFromFavourites(any(String.class), any(String.class))).thenReturn(false);

        ResponseEntity<String> response = movieController.deleteMovieFromFavourites(email, movieId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    void testGetFavouriteMovieByMovieId_Success() throws MovieNotFoundException {
        // Arrange
        String movieId = "123";
        FavouriteMovies expectedMovie = new FavouriteMovies("123", "Movie Title", "test@example.com", Arrays.asList(1, 2, 3));
        when(movieService.getFavouriteMovieByMovieId(movieId)).thenReturn(expectedMovie);

        // Act
        ResponseEntity<?> responseEntity = movieController.getFavouriteMovieByMovieId(movieId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedMovie, responseEntity.getBody());
    }
    @Test
    void testRegisterFavourite_Success() throws MovieAlreadyExistsException, UserNotFoundException {
        // Arrange
        FavouriteMovies favouriteMovie = new FavouriteMovies("123", "Movie Title", "test@example.com", Arrays.asList(1, 2, 3));
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        // Mocking the behavior of the request.getAttribute method
        when(request.getAttribute(any())).thenReturn("test@example.com");
        when(movieService.registerFavourite(any(), any())).thenReturn(favouriteMovie);

        // Act
        ResponseEntity<?> responseEntity = movieController.registerFavourite(favouriteMovie, request);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(favouriteMovie, responseEntity.getBody());
    }


    @Test
    void testRegisterFavourite_Failure() {
        // Arrange
        FavouriteMovies favouriteMovie = new FavouriteMovies();
        HttpServletRequest request = null;

        // Act
        ResponseEntity<?> responseEntity = movieController.registerFavourite(favouriteMovie, request);

        // Assert
        assert responseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR : "HTTP status code should be INTERNAL_SERVER_ERROR";
        assert responseEntity.getBody() != null : "Response body should not be null";
        assert responseEntity.getBody().toString().contains("Unexpected error occurred") : "Response body should contain expected error message";
    }
    @Test
    void testGetAllFavouriteMovies_Success() throws UserNotFoundException {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class); // Creating a mock HttpServletRequest
        List<FavouriteMovies> expectedMovies = new ArrayList<>();
        expectedMovies.add(new FavouriteMovies("1", "Movie 1", "test@example.com", Arrays.asList(1, 2, 3)));
        expectedMovies.add(new FavouriteMovies("2", "Movie 2", "test@example.com", Arrays.asList(1, 2, 3)));

        // Mocking the getUserIdFromClaims method to return a valid user ID
        when(request.getAttribute("userId")).thenReturn("test@example.com");

        // Mocking the service method to return the expected movies
        when(movieService.getAllFavouriteMovies("test@example.com")).thenReturn(expectedMovies);

        // Act
        ResponseEntity<List<FavouriteMovies>> responseEntity = movieController.getAllFavouriteMovies(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedMovies, responseEntity.getBody());
    }

    @Test
    void testGetAllFavouriteMovies_UserNotFound() throws UserNotFoundException {
        // Arrange
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        String userId = "invalidUserId";

        // Mocking getUserIdFromClaims to return an invalid user ID
        when(mockRequest.getAttribute("userId")).thenReturn(userId);
        // Mocking the service method to throw UserNotFoundException
        when(movieService.getAllFavouriteMovies(userId)).thenThrow(new UserNotFoundException());

        // Act
        ResponseEntity<List<FavouriteMovies>> response = movieController.getAllFavouriteMovies(mockRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody()); // Response body should be null for a 404 status
    }


}







