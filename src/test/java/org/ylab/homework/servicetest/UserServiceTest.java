package org.ylab.homework.servicetest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ylab.homework.homework_2.database.UserRepository;
import org.ylab.homework.homework_2.model.Role;
import org.ylab.homework.homework_2.model.Training;
import org.ylab.homework.homework_2.model.User;
import org.ylab.homework.homework_2.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Testing UserService class")
public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Test registering a new user")
    public void testRegister() throws SQLException {
        List<Training> trainings = new ArrayList<>();
        User user = new User(1, "testUser", "password", Role.USER, trainings);
        userService.register(user);
        verify(userRepository).addUser(user);
    }

    @Test
    @DisplayName("Test logging in")
    public void testLogin() throws SQLException {
        String username = "testUser";
        List<Training> trainings = new ArrayList<>();
        User expectedUser = new User(1, username, "password", Role.USER, trainings);

        when(userRepository.getUser(username)).thenReturn(expectedUser);
        User actualUser = userService.login(username);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("Test updating user role")
    public void testUpdateUserRole() throws SQLException {
        String username = "testUser";
        Role newRole = Role.ADMIN;
        userService.updateUserRole(username, newRole);
        verify(userRepository).updateUserRole(username, newRole);
    }

    @Test
    @DisplayName("Test getting all users")
    public void testGetAllUsers() throws SQLException {
        List<User> expectedUsers = new ArrayList<>();
        List<Training> trainings1 = new ArrayList<>();
        List<Training> trainings2 = new ArrayList<>();
        expectedUsers.add(new User(1, "user1", "password1", Role.USER, trainings1));
        expectedUsers.add(new User(2, "user2", "password2", Role.USER, trainings2));

        when(userRepository.getAllUsers()).thenReturn(expectedUsers);
        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    @DisplayName("Test getting a user by username")
    public void testGetUser() throws SQLException {
        String username = "testUser";
        List<Training> trainings = new ArrayList<>();
        User expectedUser = new User(1, username, "password", Role.USER, trainings);
        when(userRepository.getUser(username)).thenReturn(expectedUser);
        User actualUser = userService.getUser(username);
        assertEquals(expectedUser, actualUser);
    }
}


