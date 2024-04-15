package org.ylab.homework.homework_1.service;

import org.ylab.homework.homework_1.database.TrainingDB;
import org.ylab.homework.homework_1.model.Training;
import org.ylab.homework.homework_1.model.User;

import java.util.List;
import java.util.Random;

/**
 * Сервис для работы с тренировками.
 */
public class TrainingService {
    private TrainingDB trainingDB;

    /**
     * Создает новый экземпляр TrainingService.
     * @param trainingDB объект, предоставляющий доступ к базе данных тренировок
     */
    public TrainingService(TrainingDB trainingDB) {
        this.trainingDB = trainingDB;
    }

    /**
     * Добавляет новую тренировку для пользователя.
     * @param user пользователь, для которого добавляется тренировка
     * @param training тренировка, которая добавляется
     */
    public void addTraining(User user, Training training) {
        trainingDB.addTraining(user, training);
    }

    /**
     * Возвращает список всех тренировок пользователя.
     * @param user пользователь, для которого запрашивается список тренировок
     * @return список тренировок пользователя
     */
    public List<Training> getTrainings(User user) {
        return trainingDB.getTrainings(user);
    }

    /**
     * Удаляет тренировку пользователя.
     * @param user пользователь, у которого удаляется тренировка
     * @param training тренировка, которая удаляется
     */
    public void deleteTraining(User user, Training training) {
        trainingDB.deleteTraining(user, training);
    }

    /**
     * Редактирует существующую тренировку пользователя
     * @param user пользователь, у которого редактируется тренировка
     * @param oldTraining старая версия тренировки
     * @param newTraining новая версия тренировки
     */
    public void editTraining(User user, Training oldTraining, Training newTraining) {
        trainingDB.updateTraining(user, oldTraining, newTraining);
    }

    /**
     * Возвращает список всех тренировок всех пользователей.
     * @return список всех тренировок
     */
    public List<Training> getAllTrainings() {
        return trainingDB.getAllTrainings();
    }

    /**
     * Генерирует уникальный идентификатор для тренировки.
     * @return уникальный идентификатор
     */
    public int generateId() {
        Random random = new Random();
        return random.nextInt(10000);
    }
}
