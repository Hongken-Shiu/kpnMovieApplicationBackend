package com.kpn.movieservice.repository;

import com.kpn.movieservice.domain.FavouriteMovies;
import com.kpn.movieservice.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    private FavouriteMovies favouriteMovie1, favouriteMovie2;
    private User user;
    List<FavouriteMovies> favouriteMoviesList;

    @BeforeEach
    public void setUp() {
        favouriteMovie1 = new FavouriteMovies();
        favouriteMovie1.setMovieId("123");
        favouriteMovie1.setMovieName("Favourite Movie 1");

        favouriteMovie2 = new FavouriteMovies();
        favouriteMovie2.setMovieId("456");
        favouriteMovie2.setMovieName("Favourite Movie 2");

        favouriteMoviesList = Arrays.asList(favouriteMovie1, favouriteMovie2);

        user = new User();
        user.setEmail("sam@gmail.com");
        user.setUserName("John");
        user.setPassword("Spass");
        user.setPhoneNumber("1234");
        user.setGender("male");
        user.setProfilePicture("profile.jpg");
        user.setFavouriteMovies(favouriteMoviesList);
    }

    @AfterEach
    public void tearDown() {
        movieRepository.deleteAll();
    }

    @Test
    public void testRegisterUserSuccess() {
        movieRepository.insert(user);
        User savedUser = movieRepository.findById(user.getEmail()).get();
        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    public void testGetFavouriteMoviesByUserEmailSuccess() {
        movieRepository.insert(user);
        User retrievedUser = movieRepository.findById(user.getEmail()).get();
        List<FavouriteMovies> moviesList = retrievedUser.getFavouriteMovies();
        assertEquals(2, moviesList.size());
    }

    @Test
    public void testDeleteFavouriteMovieByUserEmailSuccess() {
        movieRepository.insert(user);
        User retrievedUser = movieRepository.findById(user.getEmail()).get();
        List<FavouriteMovies> moviesList = retrievedUser.getFavouriteMovies();
        moviesList.removeIf(movie -> movie.getMovieId().equals("123"));
        assertEquals(1, moviesList.size());
    }
}
