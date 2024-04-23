package org.ylab.homework.servicetest;
import org.ylab.homework.homework_2.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.SQLException;
import java.util.Set;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ylab.homework.homework_2.database.TrainingTypeRepository;
import org.ylab.homework.homework_2.model.TrainingType;
import java.util.Arrays;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayName("Testing TrainingTypeService class")
public class TrainingTypeServiceTest {

    private TrainingTypeService trainingTypeService;
    @Mock
    private TrainingTypeRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        trainingTypeService = new TrainingTypeService(repository);
    }

    @Test
    @DisplayName("Test getting training types")
    public void testGetTrainingTypes() throws SQLException {
        when(repository.getAllTrainingTypes()).thenReturn(Arrays.asList(
                new TrainingType("Running"),
                new TrainingType("Swimming"),
                new TrainingType("Cycling")
        ));
        Set<String> trainingTypes = trainingTypeService.getTrainingTypes();
        assertEquals(new HashSet<>(Arrays.asList("Running", "Swimming", "Cycling")), trainingTypes);
    }

    @Test
    @DisplayName("Test adding a new training type")
    public void testAddTrainingType() throws SQLException {
        String trainingType = "Running";
        when(repository.containsTrainingType(trainingType)).thenReturn(false);
        trainingTypeService.addTrainingType(trainingType);
        verify(repository, times(1)).addTrainingType(Mockito.any(TrainingType.class));
    }


    @Test
    @DisplayName("Test adding an existing training type")
    public void testAddExistingTrainingType() throws SQLException {
        String trainingType = "Running";
        when(repository.containsTrainingType(trainingType)).thenReturn(true);
        trainingTypeService.addTrainingType(trainingType);
        verify(repository, never()).addTrainingType(any());
    }

    @Test
    @DisplayName("Test checking if a training type exists")
    public void testContainsTrainingType() throws SQLException {
        String existingType = "Running";
        String nonExistingType = "Yoga";
        when(repository.getAllTrainingTypes()).thenReturn(Arrays.asList(
                new TrainingType("Running"),
                new TrainingType("Swimming"),
                new TrainingType("Cycling")
        ));
        assertTrue(trainingTypeService.containsTrainingType(existingType));
        assertFalse(trainingTypeService.containsTrainingType(nonExistingType));
    }
}
