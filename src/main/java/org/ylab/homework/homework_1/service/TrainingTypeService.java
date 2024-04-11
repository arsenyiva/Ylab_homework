package org.ylab.homework.homework_1.service;

import java.util.HashSet;
import java.util.Set;

/**
 * Сервис для работы с видами тренировок.
 */
public class TrainingTypeService {
    private Set<String> trainingTypes;

    /**
     * Создает новый экземпляр TrainingTypeService.
     */
    public TrainingTypeService() {
        this.trainingTypes = new HashSet<>();
    }

    /**
     * Возвращает множество всех видов тренировок.
     * @return множество видов тренировок
     */
    public Set<String> getTrainingTypes() {
        return trainingTypes;
    }

    /**
     * Добавляет новый вид тренировки.
     * @param trainingType добавляемый вид тренировки
     */
    public void addTrainingType(String trainingType) {
        trainingTypes.add(trainingType.toUpperCase());
    }

    /**
     * Проверяет наличие указанного вида тренировки.
     * @param trainingType проверяемый вид тренировки
     * @return true, если вид тренировки уже существует, иначе false
     */
    public boolean containsTrainingType(String trainingType) {
        return trainingTypes.contains(trainingType.toUpperCase());
    }
}
