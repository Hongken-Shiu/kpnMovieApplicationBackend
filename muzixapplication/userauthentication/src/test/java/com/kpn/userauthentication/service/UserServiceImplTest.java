package com.kpn.userauthentication.service;

import com.kpn.userauthentication.domain.User;
import com.kpn.userauthentication.exception.InvalidCredentialsException;
import com.kpn.userauthentication.exception.UserAlreadyExistException;
import com.kpn.userauthentication.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User("test@mail.com","test111");
    }
    @AfterEach
    public void tearDown() throws Exception {
        user = null;
    }
    @Test
    public void givenUserToSaveReturnSavedUserSuccess() throws UserAlreadyExistException{
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));
        when(userRepository.save(any())).thenReturn(user);
        assertEquals(user,userService.saveUser(user));
        verify(userRepository,times(1)).save(any());
        verify(userRepository,times(1)).findById(any());
    }
    @Test
    public void givenUserToSaveReturnSavedUserFailure() throws UserAlreadyExistException {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        assertThrows(UserAlreadyExistException.class,()->userService.saveUser(user));
        verify(userRepository,times(1)).findById(any());
        verify(userRepository,times(0)).save(any());
    }
    @Test
    public void givenUserLoginSuccessReturnRetrievedUser()
    {
        when(userRepository.findByEmailAndPassword(anyString(),any())).thenReturn(user);

        assertEquals(user,userRepository.findByEmailAndPassword(user.getEmail(),user.getPassword()));
        verify(userRepository,times(1)).findByEmailAndPassword(anyString(),any());
    }
    @Test
    public void deleteUser_Success() throws InvalidCredentialsException {
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(user));
        boolean isDeleted = userService.deleteUser(user.getEmail());
        assertTrue(isDeleted);
        verify(userRepository, times(1)).findById(user.getEmail());
        verify(userRepository, times(1)).deleteById(user.getEmail());
    }
    @Test
    public void deleteUser_UserNotFound_ThrowsInvalidCredentialsException() {
        // Arrange
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> userService.deleteUser(user.getEmail()));
        verify(userRepository, times(1)).findById(user.getEmail());
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    public void getAllUser_Success() throws InvalidCredentialsException {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        List<User> retrievedUsers = userService.getAllUser();

        assertFalse(retrievedUsers.isEmpty());
        assertEquals(userList.size(), retrievedUsers.size());
        assertEquals(userList.get(0), retrievedUsers.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getAllUser_NoUsersFound_ThrowsInvalidCredentialsException() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(InvalidCredentialsException.class, () -> userService.getAllUser());
        verify(userRepository, times(1)).findAll();
    }

}
