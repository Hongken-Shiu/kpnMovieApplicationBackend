package com.kpn.movieservice.repository;

import com.kpn.movieservice.domain.FavouriteMovies;
import com.kpn.movieservice.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<User,String> {
	Optional<User> findByFavouriteMoviesMovieId(String movieId);
}
