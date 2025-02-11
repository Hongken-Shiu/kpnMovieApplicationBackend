package com.kpn.movieservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class User {
    @Id
    private String email;
    private String userName;
    @Transient
    private String password;
    private String phoneNumber;
    private String gender;
    private String profilePicture;
    private List<FavouriteMovies> favouriteMovies;


    public User() {
    }


    public User(String email, String userName, String password, String phoneNumber, String gender, String profilePicture, List<FavouriteMovies> favouriteMovies) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.profilePicture = profilePicture;
        this.favouriteMovies = favouriteMovies;

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<FavouriteMovies> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setFavouriteMovies(List<FavouriteMovies> favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", favouriteMovies=" + favouriteMovies +
                '}';
    }
}