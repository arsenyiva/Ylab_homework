package org.ylab.homework.homework_1.controller;

import org.ylab.homework.homework_1.model.Training;
import org.ylab.homework.homework_1.model.TrainingType;
import org.ylab.homework.homework_1.model.User;
import org.ylab.homework.homework_1.service.TrainingService;
import org.ylab.homework.homework_1.service.TrainingTypeService;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс для управления тренировками пользователей в системе.
 */
public class TrainingController {
    private TrainingService trainingService;
    private TrainingTypeService trainingTypeService;

    /**
     * Конструктор для создания контроллера тренировок с указанными сервисами.
     *
     * @param trainingService Сервис для работы с тренировками
     * @param trainingTypeService Сервис для работы с типами тренировок
     */
    public TrainingController(TrainingService trainingService, TrainingTypeService trainingTypeService) {
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
    }

    /**
     * Добавляет новую тренировку для указанного пользователя.
     *
     * @param user Пользователь, для которого добавляется тренировка
     * @param typeName Название типа тренировки
     * @param date Дата тренировки
     * @param duration Продолжительность тренировки в минутах
     * @param calories Сожженные калории
     * @param info Дополнительная информация о тренировке
     */
    public void addTraining(User user, String typeName, LocalDate date, int duration, int calories, String info) {
        if (!trainingTypeService.containsTrainingType(typeName)) {
            trainingTypeService.addTrainingType(typeName);
            System.out.println("Новый вид тренировки добавлен!");
        }

        if (!isTrainingAddedForDate(user, typeName, date)) {
            Training training = new Training(trainingService.generateId(), date, new TrainingType(typeName), duration, calories, info, user);
            trainingService.addTraining(user, training);
            System.out.println("Тренировка успешно добавлена!");
        } else {
            System.out.println("Для этого типа тренировки уже была добавлена тренировка в указанную дату.");
        }
    }

    /**
     * Удаляет тренировку указанного пользователя по дате и типу тренировки.
     *
     * @param user Пользователь, у которого удаляется тренировка
     * @param date Дата тренировки для удаления
     * @param type Тип тренировки для удаления
     */
    public void deleteTraining(User user, LocalDate date, String type) {
        List<Training> userTrainings = trainingService.getTrainings(user);
        Training trainingToDelete = findTrainingByDateAndType(userTrainings, date, type);
        if (trainingToDelete != null) {
            trainingService.deleteTraining(user, trainingToDelete);
            System.out.println("Удалено успешно!.");
        } else {
            System.out.println("Тренировка не найдена...");
        }
    }

    /**
     * Редактирует тренировку указанного пользователя по дате и типу тренировки.
     *
     * @param user Пользователь, у которого редактируется тренировка
     * @param date Дата редактируемой тренировки
     * @param type Тип редактируемой тренировки
     * @param newTypeName Новое название типа тренировки
     * @param newDuration Новая продолжительность тренировки
     * @param newCalories Новое количество сожженных калорий
     * @param newInfo Новая дополнительная информация о тренировке
     */
    public void editTraining(User user, LocalDate date, String type, String newTypeName, int newDuration, int newCalories, String newInfo) {
        List<Training> userTrainings = trainingService.getTrainings(user);
        Training trainingToEdit = findTrainingByDateAndType(userTrainings, date, type);
        if (trainingToEdit != null) {
            TrainingType newType = new TrainingType(newTypeName);
            Training editedTraining = new Training(trainingService.generateId(), date, newType, newDuration, newCalories, newInfo, user);
            trainingService.editTraining(user, trainingToEdit, editedTraining);
            System.out.println("Тренировка успешно отредактирована.");
        } else {
            System.out.println("Тренировка не найдена...");
        }
    }

    /**
     * Получает набор доступных типов тренировок.
     *
     * @return Набор типов тренировок
     */
    public Set<String> getTrainingTypes() {
        return trainingTypeService.getTrainingTypes();
    }

    /**
     * Вычисляет общее количество сожженных калорий для указанного пользователя за период времени.
     *
     * @param user Пользователь, для которого вычисляется количество калорий
     * @param startDate Начальная дата периода
     * @param endDate Конечная дата периода
     * @return Общее количество сожженных калорий
     */
    public int getTotalCaloriesBurned(User user, LocalDate startDate, LocalDate endDate) {
        List<Training> trainings = trainingService.getTrainings(user);
        int totalCalories = 0;
        for (Training training : trainings) {
            LocalDate date = training.getDate();
            if (date.isEqual(startDate) || date.isEqual(endDate) || (date.isAfter(startDate) && date.isBefore(endDate))) {
                totalCalories += training.getCaloriesBurned();
            }
        }
        return totalCalories;
    }

    /**
     * Получает список всех тренировок в системе.
     * @return Список всех тренировок
     */
    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    /**
     * Проверяет, добавлена ли уже тренировка указанного типа для пользователя на указанную дату.
     *
     * @param user Пользователь
     * @param typeName Название типа тренировки
     * @param date Дата тренировки
     * @return true, если тренировка уже добавлена для указанного пользователя и даты, иначе false
     */
    public boolean isTrainingAddedForDate(User user, String typeName, LocalDate date) {
        List<Training> userTrainings = trainingService.getTrainings(user);
        for (Training training : userTrainings) {
            if (training.getDate().equals(date) && training.getType().getName().equalsIgnoreCase(typeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Находит тренировку пользователя по дате и типу тренировки.
     *
     * @param trainings Список тренировок пользователя
     * @param date Дата тренировки
     * @param type Тип тренировки
     * @return Найденная тренировка или null, если не найдена
     */
    public Training findTrainingByDateAndType(List<Training> trainings, LocalDate date, String type) {
        for (Training training : trainings) {
            if (training.getDate().equals(date) && training.getType().getName().equalsIgnoreCase(type)) {
                return training;
            }
        }
        return null;
    }

    /**
     * Получает список всех тренировок пользователя.
     * @param user Пользователь, для которого получается список тренировок
     * @return Список тренировок пользователя
     */
    public List<Training> getTrainings(User user) {
        return trainingService.getTrainings(user);
    }

    /**
     * Добавляет новый тип тренировки.
     * @param typeName Название нового типа тренировки
     */
    public void addTrainingType(String typeName) {
        trainingTypeService.addTrainingType(typeName.toUpperCase());
    }
}