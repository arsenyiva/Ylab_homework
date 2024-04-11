package org.ylab.homework.controllertest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ylab.homework.homework_1.in.controller.TrainingController;
import org.ylab.homework.homework_1.model.Role;
import org.ylab.homework.homework_1.model.Training;
import org.ylab.homework.homework_1.model.TrainingType;
import org.ylab.homework.homework_1.model.User;
import org.ylab.homework.homework_1.service.TrainingService;
import org.ylab.homework.homework_1.service.TrainingTypeService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public void testAddTraining() {
        String typeName = "Run";
        LocalDate date = LocalDate.of(2024, 4, 11);
        int duration = 60;
        int calories = 300;
        String info = "info";

        when(trainingTypeService.containsTrainingType(typeName)).thenReturn(false);
        when(trainingService.generateId()).thenReturn(1);

        trainingController.addTraining(user, typeName, date, duration, calories, info);

        verify(trainingTypeService, times(1)).addTrainingType(typeName);
        verify(trainingService, times(1)).addTraining(eq(user), Mockito.any(Training.class));
    }

    @Test
    public void testDeleteTraining() {
        LocalDate date = LocalDate.of(2024, 4, 11);
        String type = "Run";

        Training training = new Training(1, date, new TrainingType(type), 60, 300, "Morning run", user);
        List<Training> trainings = Collections.singletonList(training);

        when(trainingService.getTrainings(user)).thenReturn(trainings);

        trainingController.deleteTraining(user, date, type);

        verify(trainingService, times(1)).deleteTraining(eq(user), eq(training));
    }

    @Test
    public void testEditTraining() {
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
    public void testGetTotalCaloriesBurned() {
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
    public void testGetAllTrainings() {
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training(1, LocalDate.of(2024, 4, 11), new TrainingType("Run"), 60, 300, "info", user));
        when(trainingService.getAllTrainings()).thenReturn(trainings);
        List<Training> result = trainingController.getAllTrainings();

        assertTrue(result.containsAll(trainings));
    }

    @Test
    public void testGetTrainings() {
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training(1, LocalDate.of(2024, 4, 11), new TrainingType("Run"), 60, 300, "info", user));
        when(trainingService.getTrainings(user)).thenReturn(trainings);
        List<Training> result = trainingController.getTrainings(user);

        assertEquals(trainings, result);
    }

    @Test
    public void testAddTrainingType() {
        String typeName = "Running";
        trainingController.addTrainingType(typeName);
        verify(trainingTypeService).addTrainingType(typeName.toUpperCase());
    }
}