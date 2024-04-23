package org.ylab.homework.repositorytest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.ylab.homework.homework_2.database.UserRepository;
import org.ylab.homework.homework_2.model.Role;
import org.ylab.homework.homework_2.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@DisplayName("UserRepository Tests")
public class UserRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_database")
            .withUsername("test_user")
            .withPassword("test_password");

    private static Connection connection;
    private UserRepository repository;

    @BeforeAll
    public static void setUpDatabase() throws SQLException {

        connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        createSchema();
        createTables();
    }

    @BeforeEach
    public void setUp() throws SQLException {
        repository = new UserRepository(connection);
        clearTestData();

    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private static void createSchema() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS training_app");
        }
    }

    private static void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE training_app.users (id SERIAL PRIMARY KEY, username VARCHAR(255), password VARCHAR(255), role VARCHAR(255))");
        }
    }

    private void clearTestData() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM training_app.users";
            statement.executeUpdate(sql);
        }
    }

    @Test
    @DisplayName("Add User and Get User")
    void testAddUserAndGetUser() throws SQLException {
        User userToAdd = new User("testUser", "testPassword", Role.USER, null);
        repository.addUser(userToAdd);

        User retrievedUser = repository.getUser("testUser");

        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getUsername());
        assertEquals("testPassword", retrievedUser.getPassword());
        assertEquals(Role.USER, retrievedUser.getRole());

    }

    @Test
    @DisplayName("Update User Role")
    void testUpdateUserRole() throws SQLException {
        User userToAdd = new User("testUser", "testPassword", Role.USER, null);
        repository.addUser(userToAdd);
        repository.updateUserRole("testUser", Role.ADMIN);
        User updatedUser = repository.getUser("testUser");
        assertNotNull(updatedUser);
        assertEquals(Role.ADMIN, updatedUser.getRole());

    }

    @Test
    @DisplayName("Get All Users")
    void testGetAllUsers() throws SQLException {
        User user1 = new User("testUser1", "testPassword1", Role.USER, null);
        repository.addUser(user1);
        User user2 = new User("testUser2", "testPassword2", Role.ADMIN, null);
        repository.addUser(user2);
        List<User> userList = repository.getAllUsers();
        assertNotNull(userList);
        assertEquals(2, userList.size());
    }

}