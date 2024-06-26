package org.ylab.homework.controllertest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ylab.homework.homework_2.controller.TrainingController;
import org.ylab.homework.homework_2.model.Role;
import org.ylab.homework.homework_2.model.Training;
import org.ylab.homework.homework_2.model.TrainingType;
import org.ylab.homework.homework_2.model.User;
import org.ylab.homework.homework_2.service.TrainingService;
import org.ylab.homework.homework_2.service.TrainingTypeService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@DisplayName("Testing TrainingController class")
public class TrainingControllerTest {

    private static User user;
    private TrainingService trainingService;
    private TrainingTypeService trainingTypeService;
    private TrainingController trainingController;

    @BeforeAll
    public static void setUpBeforeClass() {
        user = new User(1, "user", "user", Role.USER, new ArrayList<>());
    }


    @BeforeEach
    public void setUp() {
        trainingService = Mockito.mock(TrainingService.class);
        trainingTypeService = Mockito.mock(TrainingTypeService.class);
        trainingController = new TrainingController(trainingService, trainingTypeService);

    }

    @Test
    @DisplayName("Test adding a new training")
    public void testAddTraining() throws SQLException {
        String typeName = "Run";
        LocalDate date = LocalDate.of(2024, 4, 11);
        int duration = 60;
        int calories = 300;
        String info = "info";

        when(trainingTypeService.containsTrainingType(typeName)).thenReturn(false);


        trainingController.addTraining(user, typeName, date, duration, calories, info);

        verify(trainingTypeService, times(1)).addTrainingType(typeName);
        verify(trainingService, times(1)).addTraining(eq(user), Mockito.any(Training.class));
    }

    @Test
    @DisplayName("Test deleting a training")
    public void testDeleteTraining() throws SQLException {
        LocalDate date = LocalDate.of(2024, 4, 11);
        String type = "Run";

        Training training = new Training(1, date, new TrainingType(type), 60, 300, "Morning run", user);
        List<Training> trainings = Collections.singletonList(training);

        when(trainingService.getTrainings(user)).thenReturn(trainings);

        trainingController.deleteTraining(user, date, type);

        verify(trainingService, times(1)).deleteTraining(eq(user), eq(training));
    }

    @Test
    @DisplayName("Test editing a training")
    public void testEditTraining() throws SQLException {
        LocalDate date = LocalDate.of(2024, 4, 11);
        LocalDate newDate = LocalDate.of(2024, 5, 11);
        String type = "Run";
        String newType = "New Run";

        Training training = new Training(1, date, new TrainingType(type), 60, 300, "info", user);
        when(trainingService.getTrainings(Mockito.any(User.class))).thenReturn(Collections.singletonList(training));
        Training newTraining = new Training(1, newDate, new TrainingType(newType), 66, 500, "new info", user);
        trainingService.editTraining(user, training, newTraining);

        verify(trainingService, times(1)).editTraining(eq(user), eq(training), eq(newTraining));
    }


    @Test
    @DisplayName("Test calculating total calories burned")
    public void testGetTotalCaloriesBurned() throws SQLException {
        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 30);

        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training(1, LocalDate.of(2024, 4, 5), new TrainingType("Running"), 60, 300, "Morning run", user));
        trainings.add(new Training(2, LocalDate.of(2024, 4, 15), new TrainingType("Cycling"), 45, 200, "Afternoon ride", user));
        when(trainingService.getTrainings(user)).thenReturn(trainings);
        int totalCalories = trainingController.getTotalCaloriesBurned(user, startDate, endDate);

        assertEquals(500, totalCalories);
    }

    @Test
    @DisplayName("Test getting all trainings")
    public void testGetAllTrainings() throws SQLException {
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training(1, LocalDate.of(2024, 4, 11), new TrainingType("Run"), 60, 300, "info", user));
        when(trainingService.getAllTrainings()).thenReturn(trainings);
        List<Training> result = trainingController.getAllTrainings();

        assertTrue(result.containsAll(trainings));
    }

    @Test
    @DisplayName("Test getting trainings for a user")
    public void testGetTrainings() throws SQLException {
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training(1, LocalDate.of(2024, 4, 11), new TrainingType("Run"), 60, 300, "info", user));
        when(trainingService.getTrainings(user)).thenReturn(trainings);
        List<Training> result = trainingController.getTrainings(user);

        assertEquals(trainings, result);
    }
}