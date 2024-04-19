package org.ylab.homework.homework_2.service;

import org.ylab.homework.homework_2.database.TrainingTypeRepository;
import org.ylab.homework.homework_2.model.TrainingType;

import java.util.HashSet;
import java.util.Set;
import java.sql.SQLException;

/**
 * Сервис для работы с видами тренировок.
 */
public class TrainingTypeService {
    private TrainingTypeRepository repository;

    public TrainingTypeService(TrainingTypeRepository repository) {
        this.repository = repository;
    }

    /**
     * Возвращает множество всех видов тренировок.
     *
     * @return множество видов тренировок
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public Set<String> getTrainingTypes() throws SQLException {
        Set<String> trainingTypes = new HashSet<>();
        for (TrainingType trainingType : repository.getAllTrainingTypes()) {
            trainingTypes.add(trainingType.getName());
        }
        return trainingTypes;
    }

    /**
     * Добавляет новый вид тренировки.
     *
     * @param trainingType добавляемый вид тренировки
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public void addTrainingType(String trainingType) throws SQLException {
        if (!repository.containsTrainingType(trainingType)) {
            TrainingType newTrainingType = new TrainingType(trainingType);
            repository.addTrainingType(newTrainingType);
        }
    }
    public boolean containsTrainingType(String trainingType) throws SQLException {
        return repository.getAllTrainingTypes().stream()
                .anyMatch(type -> type.getName().equalsIgnoreCase(trainingType));
    }

    public int getTrainingTypeId(String typeName) throws SQLException {
        return repository.getTrainingTypeId(typeName);
    }
}
