package org.ylab.homework.controllertest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ylab.homework.homework_1.in.controller.UserController;
import org.ylab.homework.homework_1.model.Role;
import org.ylab.homework.homework_1.model.Training;
import org.ylab.homework.homework_1.model.User;
import org.ylab.homework.homework_1.service.UserService;



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
    public void testRegisterUser() {
        String username = "user";
        String password = "password";
        Role role = Role.USER;
        List<Training> trainings = new ArrayList<>();

        when(userService.generateId()).thenReturn(1);
        userController.registerUser(username, password, role, trainings);

        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    public void testLoginUser_Successful() {
        String username = "user";
        String password = "password";

        when(userService.login(username)).thenReturn(initialUser);
        User loggedInUser = userController.loginUser(username, password);

        assertNotNull(loggedInUser);
        assertEquals(initialUser, loggedInUser);
    }

    @Test
    public void testLoginUser_IncorrectCredentials() {
        String username = "user";
        String password = "wrong_password";

        when(userService.login(username)).thenReturn(null);
        User loggedInUser = userController.loginUser(username, password);

        assertNull(loggedInUser);
    }

    @Test
    public void testLoginUser_WrongPassword() {
        String username = "user";
        String password = "wrong_password";

        when(userService.login(username)).thenReturn(initialUser);
        User loggedInUser = userController.loginUser(username, password);

        assertNull(loggedInUser);
    }

    @Test
    public void testUpdateUserRole_UserExists() {
        String username = "user";
        Role newRole = Role.ADMIN;

        when(userService.getUser(username)).thenReturn(initialUser);
        userController.updateUserRole(username, newRole);

        verify(userService, times(1)).updateUserRole(username, newRole);
    }

    @Test
    public void testUpdateUserRole_UserDoesNotExist() {
        String username = "non_existing_user";
        Role newRole = Role.ADMIN;

        when(userService.getUser(username)).thenReturn(null);
        userController.updateUserRole(username, newRole);

        verify(userService, never()).updateUserRole(username, newRole);
    }
}

