package org.ylab.homework.homework_1.database;

import org.ylab.homework.homework_1.model.Training;
import org.ylab.homework.homework_1.model.User;

import java.util.*;

/**
 * Класс для работы с базой данных тренировок.
 */
public class TrainingDB {
    private Map<User, List<Training>> trainingData = new HashMap<>();

    /**
     * Добавляет тренировку для указанного пользователя в базу данных.
     * Если для пользователя еще нет записей о тренировках, создается новая запись.
     *
     * @param user Пользователь, для которого добавляется тренировка
     * @param training Добавляемая тренировка
     */
    public void addTraining(User user, Training training) {
        List<Training> userTrainings = trainingData.get(user);
        if (userTrainings == null) {
            userTrainings = new ArrayList<>();
            trainingData.put(user, userTrainings);
        }
        userTrainings.add(training);
    }

    /**
     * Возвращает список тренировок для указанного пользователя.
     * @param user Пользователь, для которого запрашиваются тренировки
     * @return Список тренировок пользователя, или пустой список, если пользователь не имеет тренировок
     */
    public List<Training> getTrainings(User user) {
        return trainingData.getOrDefault(user, new ArrayList<>());
    }

    /**
     * Удаляет указанную тренировку для указанного пользователя из базы данных.
     * @param user Пользователь, чья тренировка должна быть удалена
     * @param training Тренировка, которая должна быть удалена
     */
    public void deleteTraining(User user, Training training) {
        List<Training> userTrainings = trainingData.get(user);
        if (userTrainings != null) {
            userTrainings.remove(training);
        }
    }

    /**
     * Обновляет указанную тренировку для указанного пользователя в базе данных.
     * @param user Пользователь, чья тренировка должна быть обновлена
     * @param oldTraining Старая версия тренировки
     * @param newTraining Новая версия тренировки
     */
    public void updateTraining(User user, Training oldTraining, Training newTraining) {
        List<Training> userTrainings = trainingData.get(user);
        if (userTrainings != null) {
            int index = userTrainings.indexOf(oldTraining);
            if (index != -1) {
                userTrainings.set(index, newTraining);
            }
        }
    }

    /**
     * Возвращает список всех тренировок в базе данных.
     * @return Список всех тренировок
     */
    public List<Training> getAllTrainings() {
        List<Training> allTrainings = new ArrayList<>();
        for (List<Training> userTrainings : trainingData.values()) {
            allTrainings.addAll(userTrainings);
        }
        return allTrainings;
    }
}
