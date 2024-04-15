package org.ylab.homework.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ylab.homework.homework_1.database.TrainingDB;
import org.ylab.homework.homework_1.model.Role;
import org.ylab.homework.homework_1.model.Training;
import org.ylab.homework.homework_1.model.TrainingType;
import org.ylab.homework.homework_1.model.User;
import org.ylab.homework.homework_1.service.TrainingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@DisplayName("Testing TrainingService class")
public class TrainingServiceTest {

    private TrainingDB mockTrainingDB;
    private TrainingService trainingService;
    private User user;
    private TrainingType trainingType;
    private Training training;

    @BeforeEach
    public void setUp() {
        mockTrainingDB = mock(TrainingDB.class);
        trainingService = new TrainingService(mockTrainingDB);

        user = new User(1, "testUser", "password", Role.USER, new ArrayList<>());
        trainingType = new TrainingType("Run");
        training = new Training(1, LocalDate.now(), trainingType, 30, 100, "info", user);
    }

    @Test
    @DisplayName("Test adding a training")
    public void testAddTraining() {
        trainingService.addTraining(user, training);
        verify(mockTrainingDB).addTraining(user, training);
    }

    @Test
    @DisplayName("Test getting trainings")
    public void testGetTrainings() {
        List<Training> expectedTrainings = new ArrayList<>();
        expectedTrainings.add(training);

        when(mockTrainingDB.getTrainings(user)).thenReturn(expectedTrainings);
        List<Training> actualTrainings = trainingService.getTrainings(user);

        assertEquals(expectedTrainings, actualTrainings);
    }

    @Test
    @DisplayName("Test deleting a training")
    public void testDeleteTraining() {
        trainingService.deleteTraining(user, training);
        verify(mockTrainingDB).deleteTraining(user, training);
    }

    @Test
    @DisplayName("Test editing a training")
    public void testEditTraining() {
        TrainingType newTrainingType = new TrainingType("Swimming");
        Training newTraining = new Training(1, LocalDate.now(), newTrainingType, 45, 150, "Great swim", user);
        trainingService.editTraining(user, training, newTraining);

        verify(mockTrainingDB).updateTraining(user, training, newTraining);
    }

    @Test
    @DisplayName("Test getting all trainings")
    public void testGetAllTrainings() {
        List<Training> expectedTrainings = new ArrayList<>();
        User user1 = new User(1, "user1", "password1", Role.USER, new ArrayList<>());
        User user2 = new User(2, "user2", "password2", Role.USER, new ArrayList<>());
        TrainingType trainingType1 = new TrainingType("Run");
        TrainingType trainingType2 = new TrainingType("Swim");
        expectedTrainings.add(new Training(1, LocalDate.now(), trainingType1, 30, 100, "Good workout", user1));
        expectedTrainings.add(new Training(2, LocalDate.now(), trainingType2, 45, 150, "Great swim", user2));

        when(mockTrainingDB.getAllTrainings()).thenReturn(expectedTrainings);
        List<Training> actualTrainings = trainingService.getAllTrainings();

        assertEquals(expectedTrainings, actualTrainings);
    }
}