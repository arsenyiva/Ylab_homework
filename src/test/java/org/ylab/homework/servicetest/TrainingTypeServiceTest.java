package org.ylab.homework.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ylab.homework.homework_1.service.TrainingTypeService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingTypeServiceTest {

    private TrainingTypeService trainingTypeService;

    @BeforeEach
    public void setUp() {
        trainingTypeService = new TrainingTypeService();
    }

    @Test
    public void testGetTrainingTypes() {
        Set<String> trainingTypes = trainingTypeService.getTrainingTypes();
        assertNotNull(trainingTypes);
        assertEquals(0, trainingTypes.size());
    }

    @Test
    public void testAddTrainingType() {
        String trainingType = "Run";
        trainingTypeService.addTrainingType(trainingType);
        assertTrue(trainingTypeService.containsTrainingType(trainingType));
    }

    @Test
    public void testContainsTrainingType() {
        String trainingType = "Run";

        assertFalse(trainingTypeService.containsTrainingType(trainingType));
        trainingTypeService.addTrainingType(trainingType);
        assertTrue(trainingTypeService.containsTrainingType(trainingType));
    }
}
