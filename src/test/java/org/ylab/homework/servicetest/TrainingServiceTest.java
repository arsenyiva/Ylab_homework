package org.ylab.homework.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ylab.homework.homework_2.database.TrainingRepository;
import org.ylab.homework.homework_2.model.Role;
import org.ylab.homework.homework_2.model.Training;
import org.ylab.homework.homework_2.model.TrainingType;
import org.ylab.homework.homework_2.model.User;
import org.ylab.homework.homework_2.service.TrainingService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@DisplayName("Testing TrainingService class")
public class TrainingServiceTest {

    private TrainingRepository trainingRepository;
    private TrainingService trainingService;
    private User user;
    private TrainingType trainingType;
    private Training training;

    @BeforeEach
    public void setUp() {
        trainingRepository = mock(TrainingRepository.class);
        trainingService = new TrainingService(trainingRepository);

        user = new User(1, "testUser", "password", Role.USER, new ArrayList<>());
        trainingType = new TrainingType("Run");
        training = new Training(1, LocalDate.now(), trainingType, 30, 100, "info", user);
    }

    @Test
    @DisplayName("Test adding a training")
    public void testAddTraining() throws SQLException {
        trainingService.addTraining(user, training);
        verify(trainingRepository).addTraining(user, training);
    }

    @Test
    @DisplayName("Test getting trainings")
    public void testGetTrainings() throws SQLException {
        List<Training> expectedTrainings = new ArrayList<>();
        expectedTrainings.add(training);

        when(trainingRepository.getTrainings(user)).thenReturn(expectedTrainings);
        List<Training> actualTrainings = trainingService.getTrainings(user);

        assertEquals(expectedTrainings, actualTrainings);
    }

    @Test
    @DisplayName("Test deleting a training")
    public void testDeleteTraining() throws SQLException {
        trainingService.deleteTraining(user, training);
        verify(trainingRepository).deleteTraining(user, training);
    }

    @Test
    @DisplayName("Test editing a training")
    public void testEditTraining() throws SQLException {
        TrainingType newTrainingType = new TrainingType("Swimming");
        Training newTraining = new Training(1, LocalDate.now(), newTrainingType, 45, 150, "Great swim", user);
        trainingService.editTraining(user, training, newTraining);

        verify(trainingRepository).updateTraining(user, training, newTraining);
    }

    @Test
    @DisplayName("Test getting all trainings")
    public void testGetAllTrainings() throws SQLException {
        List<Training> expectedTrainings = new ArrayList<>();
        User user1 = new User(1, "user1", "password1", Role.USER, new ArrayList<>());
        User user2 = new User(2, "user2", "password2", Role.USER, new ArrayList<>());
        TrainingType trainingType1 = new TrainingType("Run");
        TrainingType trainingType2 = new TrainingType("Swim");
        expectedTrainings.add(new Training(1, LocalDate.now(), trainingType1, 30, 100, "Good workout", user1));
        expectedTrainings.add(new Training(2, LocalDate.now(), trainingType2, 45, 150, "Great swim", user2));

        when(trainingRepository.getAllTrainings()).thenReturn(expectedTrainings);
        List<Training> actualTrainings = trainingService.getAllTrainings();

        assertEquals(expectedTrainings, actualTrainings);
    }
}