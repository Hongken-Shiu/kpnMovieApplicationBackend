package com.kpn.movieservice.service;

import com.kpn.movieservice.domain.FavouriteMovies;
import com.kpn.movieservice.domain.User;
import com.kpn.movieservice.exception.MovieAlreadyExistsException;
import com.kpn.movieservice.exception.MovieNotFoundException;
import com.kpn.movieservice.exception.UserAlreadyExistsException;
import com.kpn.movieservice.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IMovieService {

	User registerUser(User user) throws UserAlreadyExistsException;
	FavouriteMovies registerFavourite(FavouriteMovies favouriteMovie,String email) throws MovieAlreadyExistsException, UserNotFoundException;
	boolean deleteMovieFromFavourites(String movieId,String Email) throws MovieNotFoundException;
//	List<User> getAllUser(String email) throws UserNotFoundException;
	List<FavouriteMovies> getAllFavouriteMovies(String userId) throws UserNotFoundException;
	FavouriteMovies getFavouriteMovieByMovieId(String movieId) throws MovieNotFoundException;
}