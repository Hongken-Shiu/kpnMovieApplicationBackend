package com.kpn.movieservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpn.movieservice.domain.FavouriteMovies;
import com.kpn.movieservice.domain.User;
import com.kpn.movieservice.exception.MovieAlreadyExistsException;
import com.kpn.movieservice.exception.MovieNotFoundException;
import com.kpn.movieservice.exception.UserAlreadyExistsException;
import com.kpn.movieservice.exception.UserNotFoundException;
import com.kpn.movieservice.service.IMovieServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private IMovieServiceImpl iMovieService;
    @InjectMocks
    private UserController userController;
    @Mock
    private HttpServletRequest request;
    User user;
    FavouriteMovies favouriteMovies;
@BeforeEach
    void setUp(){
    user = new User();
    user.setUserName("John");
    user.setEmail("john@example.com");
    user.setPassword("password");
    user.setPhoneNumber("1234567890");
    user.setGender("Male");
    user.setProfilePicture("profile.jpg");
    user.setFavouriteMovies(new ArrayList<>());
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

}
    @AfterEach
    void tearDown() {
        user = null;
    }
    @Test
    public void registerUserSuccess() throws Exception {
        when(iMovieService.registerUser(any())).thenReturn(user);
        mockMvc.perform(post("/api/v2/register") // Update the endpoint path here
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJSONString(user)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void registerUserFailure() throws Exception {
        when(iMovieService.registerUser(any())).thenThrow(UserAlreadyExistsException.class);
        mockMvc.perform(post("/api/v2/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJSONString(user)))
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print());
    }
    private static String asJSONString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}