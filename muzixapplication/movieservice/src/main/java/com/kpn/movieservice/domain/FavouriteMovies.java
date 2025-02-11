package com.kpn.movieservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class FavouriteMovies {
    @Id
    private String movieId;
    private String movieName;
    private String email;
    private List<Integer> genreIds;

    public FavouriteMovies(String movieId, String movieName, String email, List<Integer> genreIds) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.email = email;
        this.genreIds = genreIds;
    }

    @Override
    public String toString() {
        return "FavouriteMovies{" +
                "movieId='" + movieId + '\'' +
                ", movieName='" + movieName + '\'' +
                ", email='" + email + '\'' +
                ", genreIds=" + genreIds +
                '}';
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public FavouriteMovies() {
    }


    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

}