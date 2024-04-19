package org.ylab.homework.repositorytest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.ylab.homework.homework_2.database.TrainingRepository;
import org.ylab.homework.homework_2.model.Role;
import org.ylab.homework.homework_2.model.Training;
import org.ylab.homework.homework_2.model.TrainingType;
import org.ylab.homework.homework_2.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class TrainingRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_database")
            .withUsername("test_user")
            .withPassword("test_password");

    private static Connection connection;
    private TrainingRepository repository;

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
        repository = new TrainingRepository(connection);
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
            statement.executeUpdate("CREATE TABLE training_app.trainings (id SERIAL PRIMARY KEY, user_id INT, date DATE, type_id INT, duration_minutes INT, calories_burned INT, additional_info VARCHAR(255))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS training_app.training_types (id SERIAL PRIMARY KEY, name VARCHAR(255))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS training_app.users (id SERIAL PRIMARY KEY, username VARCHAR(255), password VARCHAR(255), role VARCHAR(50))");
            statement.executeUpdate("INSERT INTO training_app.training_types (name) VALUES ('Running'), ('Swimming'), ('Cycling')");

        }
    }

    private void clearTestData() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM training_app.trainings";
            statement.executeUpdate(sql);
        }
    }


    @Test
    void testAddTrainingAndGetTrainings() throws SQLException {
        User user = new User("testUser", "testPassword", Role.USER, null);
        TrainingType trainingType = new TrainingType("Running", 1);
        Training training = new Training(user.getId(),LocalDate.now(), trainingType, 60, 200, "Morning run", user);

        repository.addTraining(user, training);

        List<Training> userTrainings = repository.getTrainings(user);

        assertNotNull(userTrainings);
        assertEquals(1, userTrainings.size());

        Training retrievedTraining = userTrainings.get(0);
        assertEquals(training.getDate(), retrievedTraining.getDate());
        assertEquals(training.getType().getId(), retrievedTraining.getType().getId());
        assertEquals(training.getDurationMinutes(), retrievedTraining.getDurationMinutes());
        assertEquals(training.getCaloriesBurned(), retrievedTraining.getCaloriesBurned());
        assertEquals(training.getAdditionalInfo(), retrievedTraining.getAdditionalInfo());
        assertEquals(training.getUser(), retrievedTraining.getUser());
    }
    @Test
    void testUpdateTraining() throws SQLException {
        User user = new User("testUser", "testPassword", Role.USER, null);

        TrainingType oldTrainingType = new TrainingType("Running", 1);
        Training oldTraining = new Training(user.getId(), LocalDate.now(), oldTrainingType, 60, 200, "Morning run", user);

        repository.addTraining(user, oldTraining);
        List<Training> userTrainings = repository.getTrainings(user);

        assertNotNull(userTrainings);
        assertEquals(1, userTrainings.size());

        Training retrievedTraining = userTrainings.get(0);
        TrainingType newTrainingType = new TrainingType("Swimming", 2);
        Training newTraining = new Training(user.getId(), LocalDate.now(), newTrainingType, 90, 300, "Morning swim", user);

        repository.updateTraining(user, retrievedTraining, newTraining);

        List<Training> updatedUserTrainings = repository.getTrainings(user);
        assertNotNull(updatedUserTrainings);
        assertEquals(1, updatedUserTrainings.size());
        Training updatedTraining = updatedUserTrainings.get(0);

        assertEquals(newTraining.getDate(), updatedTraining.getDate());
        assertEquals(newTraining.getType().getId(), updatedTraining.getType().getId());
        assertEquals(newTraining.getDurationMinutes(), updatedTraining.getDurationMinutes());
        assertEquals(newTraining.getCaloriesBurned(), updatedTraining.getCaloriesBurned());
        assertEquals(newTraining.getAdditionalInfo(), updatedTraining.getAdditionalInfo());
        assertEquals(newTraining.getUser(), updatedTraining.getUser());
    }

    @Test
    void testDeleteTraining() throws SQLException {
        User user = new User("testUser", "testPassword", Role.USER, null);

        TrainingType trainingType = new TrainingType("Running", 1);
        Training training = new Training(user.getId(), LocalDate.now(), trainingType, 60, 200, "Morning run", user);

        repository.addTraining(user, training);

        List<Training> userTrainings = repository.getTrainings(user);

        assertNotNull(userTrainings);
        assertEquals(1, userTrainings.size());

        Training retrievedTraining = userTrainings.get(0);

        repository.deleteTraining(user, retrievedTraining);
        List<Training> updatedUserTrainings = repository.getTrainings(user);

        assertNotNull(updatedUserTrainings);
        assertTrue(updatedUserTrainings.isEmpty());
    }

    @Test
    void testGetAllTrainings() throws SQLException {
        User user = new User("testUser", "testPassword", Role.USER, null);

        TrainingType runningType = new TrainingType("Running", 1);
        TrainingType swimmingType = new TrainingType("Swimming", 2);
        Training runningTraining = new Training(user.getId(), LocalDate.now(), runningType, 60, 200, "Morning run", user);
        Training swimmingTraining = new Training(user.getId(), LocalDate.now(), swimmingType, 45, 150, "Evening swim", user);

        repository.addTraining(user, runningTraining);
        repository.addTraining(user, swimmingTraining);

        List<Training> allTrainings = repository.getAllTrainings();

        assertNotNull(allTrainings);
        assertEquals(2, allTrainings.size());
    }

}
