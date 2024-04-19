package org.ylab.homework.controllertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ylab.homework.homework_2.controller.UserController;
import org.ylab.homework.homework_2.model.Role;
import org.ylab.homework.homework_2.model.Training;
import org.ylab.homework.homework_2.model.User;
import org.ylab.homework.homework_2.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayName("Testing UserController class")
public class UserControllerTest {

    private UserService userService;
    private UserController userController;
    private User initialUser;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        initialUser = new User(1, "user", "password", Role.USER, new ArrayList<>());
    }

    @Test
    @DisplayName("Test registering a new user")
    public void testRegisterUser() throws SQLException {
        String username = "user";
        String password = "password";
        Role role = Role.USER;
        List<Training> trainings = new ArrayList<>();

        userController.registerUser(username, password, role, trainings);

        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    @DisplayName("Test login with correct credentials")
    public void testLoginUser_Successful() throws SQLException {
        String username = "user";
        String password = "password";

        when(userService.login(username)).thenReturn(initialUser);
        User loggedInUser = userController.loginUser(username, password);

        assertNotNull(loggedInUser);
        assertEquals(initialUser, loggedInUser);
    }

    @Test
    @DisplayName("Test login with incorrect credentials")
    public void testLoginUser_IncorrectCredentials() throws SQLException {
        String username = "user";
        String password = "wrong_password";

        when(userService.login(username)).thenReturn(null);
        User loggedInUser = userController.loginUser(username, password);

        assertNull(loggedInUser);
    }

    @Test
    @DisplayName("Test login with incorrect password")
    public void testLoginUser_WrongPassword() throws SQLException {
        String username = "user";
        String password = "wrong_password";

        when(userService.login(username)).thenReturn(initialUser);
        User loggedInUser = userController.loginUser(username, password);

        assertNull(loggedInUser);
    }

    @Test
    @DisplayName("Test updating user role when user exists")
    public void testUpdateUserRole_UserExists() throws SQLException {
        String username = "user";
        Role newRole = Role.ADMIN;

        when(userService.getUser(username)).thenReturn(initialUser);
        userController.updateUserRole(username, newRole);

        verify(userService, times(1)).updateUserRole(username, newRole);
    }

    @Test
    @DisplayName("Test updating user role when not user exists")
    public void testUpdateUserRole_UserDoesNotExist() throws SQLException {
        String username = "non_existing_user";
        Role newRole = Role.ADMIN;

        when(userService.getUser(username)).thenReturn(null);
        userController.updateUserRole(username, newRole);

        verify(userService, never()).updateUserRole(username, newRole);
    }
}

