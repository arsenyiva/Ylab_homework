package org.ylab.homework.homework_2.service;

import org.ylab.homework.homework_2.database.TrainingRepository;
import org.ylab.homework.homework_2.model.Training;
import org.ylab.homework.homework_2.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Сервис для работы с тренировками.
 */

public class TrainingService {
    private TrainingRepository trainingRepository;

    /**
     * Создает новый экземпляр TrainingService.
     *
     * @param trainingRepository объект, предоставляющий доступ к базе данных тренировок
     */
    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    /**
     * Добавляет новую тренировку для пользователя.
     *
     * @param user     пользователь, для которого добавляется тренировка
     * @param training тренировка, которая добавляется
     */
    public void addTraining(User user, Training training) throws SQLException {
        trainingRepository.addTraining(user, training);
    }

    /**
     * Возвращает список всех тренировок пользователя.
     *
     * @param user пользователь, для которого запрашивается список тренировок
     * @return список тренировок пользователя
     */
    public List<Training> getTrainings(User user) throws SQLException {
        return trainingRepository.getTrainings(user);
    }

    /**
     * Удаляет тренировку пользователя.
     *
     * @param user     пользователь, у которого удаляется тренировка
     * @param training тренировка, которая удаляется
     */
    public void deleteTraining(User user, Training training) throws SQLException {
        trainingRepository.deleteTraining(user, training);
    }

    /**
     * Редактирует существующую тренировку пользователя.
     *
     * @param user        пользователь, у которого редактируется тренировка
     * @param oldTraining старая версия тренировки
     * @param newTraining новая версия тренировки
     */
    public void editTraining(User user, Training oldTraining, Training newTraining) throws SQLException {
        trainingRepository.updateTraining(user, oldTraining, newTraining);
    }

    /**
     * Возвращает список всех тренировок всех пользователей.
     *
     * @return список всех тренировок
     */
    public List<Training> getAllTrainings() throws SQLException {
        return trainingRepository.getAllTrainings();
    }


}

