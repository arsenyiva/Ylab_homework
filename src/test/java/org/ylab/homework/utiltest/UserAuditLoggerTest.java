package org.ylab.homework.utiltest;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.ylab.homework.homework_2.util.UserAuditLogger;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class UserAuditLoggerTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_database")
            .withUsername("test_user")
            .withPassword("test_password");
    private UserAuditLogger userAuditLogger;

    private static Connection connection;

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
        userAuditLogger = new UserAuditLogger(connection);
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
            statement.executeUpdate("CREATE TABLE training_app.audit_log (id SERIAL PRIMARY KEY, message VARCHAR(255) NOT NULL, created_at TIMESTAMP NOT NULL)");
        }
    }

    private void clearTestData() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM training_app.audit_log";
            statement.executeUpdate(sql);
        }
    }
    @Test
    void logUserRegistration() throws SQLException {
        userAuditLogger.logUserRegistration("test_user");
        assertTrue(isMessageLogged("Пользователь test_user зарегистрировался "));
    }

    @Test
    void logUserLoggedIn() throws SQLException {
        UserAuditLogger.logUserLoggedIn("test_user");
        assertTrue(isMessageLogged("Пользователь test_user произвел вход в приложение в "));
    }

    @Test
    void logTrainingAdded() throws SQLException {
        UserAuditLogger.logTrainingAdded("test_user", LocalDate.now(), "Running");
        assertTrue(isMessageLogged("Пользователь test_user добавил тренировку типа - Running "));
    }

    @Test
    void logTrainingDeleted() throws SQLException {
        UserAuditLogger.logTrainingDeleted("test_user", LocalDate.now(), "Running");
        assertTrue(isMessageLogged("Пользователь test_user удалил тренировку Running "));
    }

    @Test
    void logTrainingEdited() throws SQLException {
        UserAuditLogger.logTrainingEdited("test_user", LocalDate.now(), "Running");
        assertTrue(isMessageLogged("Пользователь test_user отредактировал тренировку Running "));
    }

    @Test
    void logFailedLoginAttempt() throws SQLException {
        UserAuditLogger.logFailedLoginAttempt("test_user");
        assertTrue(isMessageLogged("Пользователь test_user пытался произвести вход в приложение в "));
    }

    @Test
    void logLogout() throws SQLException {
        UserAuditLogger.logLogout("test_user");
        assertTrue(isMessageLogged("Пользователь test_user вышел из приложения в "));
    }

    private boolean isMessageLogged(String messagePrefix) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM training_app.audit_log WHERE message LIKE ?")) {
            preparedStatement.setString(1, messagePrefix + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
