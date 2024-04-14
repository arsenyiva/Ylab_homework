package org.ylab.homework.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ylab.homework.homework_1.service.TrainingTypeService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Testing TrainingTypeService class")
public class TrainingTypeServiceTest {

    private TrainingTypeService trainingTypeService;

    @BeforeEach
    public void setUp() {
        trainingTypeService = new TrainingTypeService();
    }

    @Test
    @DisplayName("Test getting training types")
    public void testGetTrainingTypes() {
        Set<String> trainingTypes = trainingTypeService.getTrainingTypes();
        assertNotNull(trainingTypes);
        assertEquals(0, trainingTypes.size());
    }

    @Test
    @DisplayName("Test adding a new training type")
    public void testAddTrainingType() {
        String trainingType = "Run";
        trainingTypeService.addTrainingType(trainingType);
        assertTrue(trainingTypeService.containsTrainingType(trainingType));
    }

    @Test
    @DisplayName("Test checking if a training type exists")
    public void testContainsTrainingType() {
        String trainingType = "Run";

        assertFalse(trainingTypeService.containsTrainingType(trainingType));
        trainingTypeService.addTrainingType(trainingType);
        assertTrue(trainingTypeService.containsTrainingType(trainingType));
    }
}
