package org.ylab.homework.repositorytest;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.ylab.homework.homework_2.database.TrainingTypeRepository;
import org.ylab.homework.homework_2.model.TrainingType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;


@Testcontainers
@DisplayName("TrainingTypeRepository Tests")
public class TrainingTypeRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_database")
            .withUsername("test_user")
            .withPassword("test_password");

    private static Connection connection;
    private TrainingTypeRepository repository;

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
        repository = new TrainingTypeRepository(connection);
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
            statement.executeUpdate("CREATE TABLE training_app.training_types (id SERIAL PRIMARY KEY, name VARCHAR(255))");
        }
    }
    private void clearTestData() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM training_app.training_types";
            statement.executeUpdate(sql);
        }
    }

    @Test
    @DisplayName("Get All Training Types")
    public void testGetAllTrainingTypes() throws SQLException {
        repository.addTrainingType(new TrainingType("Running"));
        List<TrainingType> trainingTypes = repository.getAllTrainingTypes();
        assertEquals(1, trainingTypes.size());
    }

    @Test
    @DisplayName("Add Training Type")
    public void testAddTrainingType() throws SQLException {
        TrainingType type = new TrainingType("Running");
        repository.addTrainingType(type);
        List<TrainingType> trainingTypes = repository.getAllTrainingTypes();
        assertEquals(1, trainingTypes.size());
        assertEquals(type.getName(), trainingTypes.get(0).getName());
    }
    @Test
    @DisplayName("Contains Training Type")
    public void testContainsTrainingType() throws SQLException {
        repository.addTrainingType(new TrainingType("Running"));
        assertTrue(repository.containsTrainingType("Running"));
        assertFalse(repository.containsTrainingType("Swimming"));
    }

    @Test
    @DisplayName("Get Training Type Id")
    public void testGetTrainingTypeId() throws SQLException {
        repository.addTrainingType(new TrainingType("Running"));
        int typeId = repository.getTrainingTypeId("Running");
        assertNotEquals(-1, typeId);
    }
}
