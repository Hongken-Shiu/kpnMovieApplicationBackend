package com.kpn.userauthentication.controller;

import com.kpn.userauthentication.domain.User;
import com.kpn.userauthentication.exception.InvalidCredentialsException;
import com.kpn.userauthentication.exception.UserAlreadyExistException;
import com.kpn.userauthentication.security.SecurityTokenGenerator;
import com.kpn.userauthentication.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private SecurityTokenGenerator securityTokenGenerator;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User("U1234", "JOHN123");
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    @AfterEach
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    void saveUser_UserSaved_SuccessResponse() throws UserAlreadyExistException {
        User user = new User();
        when(userService.saveUser(any())).thenReturn(user);

        ResponseEntity<?> response = userController.saveUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void saveUser_UserAlreadyExists_ConflictResponse() throws UserAlreadyExistException {
        User user = new User();
        when(userService.saveUser(any())).thenThrow(new UserAlreadyExistException());

        ResponseEntity<?> response = userController.saveUser(user);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

}
