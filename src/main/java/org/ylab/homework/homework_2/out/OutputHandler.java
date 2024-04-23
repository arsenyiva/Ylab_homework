package org.ylab.homework.homework_2.out;

import org.ylab.homework.homework_2.model.Training;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Класс OutputHandler предоставляет методы для отображения информации в консоли.
 */
public class OutputHandler {
    /**
     * Отображает информацию о конкретной тренировке.
     *
     * @param training Тренировка для отображения информации.
     */
    public void displayTraining(Training training) {
        System.out.println("Имя: " + training.getUser().getUsername());
        System.out.println("Дата: " + training.getDate());
        System.out.println("Вид: " + training.getType().getName());
        System.out.println("Время: " + training.getDurationMinutes() + " минут");
        System.out.println("Калорий сожжено: " + training.getCaloriesBurned());
        System.out.println("Доп инфо: " + training.getAdditionalInfo());
        System.out.println();
    }

    /**
     * Отображает список тренировок.
     *
     * @param trainings Список тренировок для отображения.
     */
    public void displayTrainings(List<Training> trainings) {
        if (trainings == null || trainings.isEmpty()) {
            System.out.println("Тренировок не найдено...");
        } else {
            trainings.sort(Comparator.comparing(Training::getDate));
            System.out.println("Тренировки:");
            for (Training training : trainings) {
                displayTraining(training);
            }
        }
    }

    /**
     * Отображает основное меню приложения.
     */
    public void displayMainMenu() {
        displayMessage("\nВыберите действие:");
        displayMessage("1. Зарегистрироваться");
        displayMessage("2. Войти");
        displayMessage("3. Выключить приложение");
        displayMessage("Введите цифру: ");
    }

    /**
     * Отображает меню для пользователя.
     */
    public void displayUserMenu() {
        displayMessage("\nМеню тренировок: ");
        displayMessage("1. Добавить тренировку ");
        displayMessage("2. Просмотр моих тренировок ");
        displayMessage("3. Удалить тренировку ");
        displayMessage("4. Редактировать тренировку ");
        displayMessage("5. Получить статистику по тренировкам ");
        displayMessage("6. Выйти из аккаунта ");
        displayMessage("Введите цифру: ");
    }

    /**
     * Отображает меню для админа.
     */
    public void displayAdminMenu() {
        displayMessage("\nПанель администратора: ");
        displayMessage("1. Просмотр всех тренировок ");
        displayMessage("2. Сменить роль пользователя ");
        displayMessage("3. Просмотр действий пользователей ");
        displayMessage("4. Выйти из аккаунта ");
        displayMessage("Введите цифру: ");
    }

    /**
     * Отображает доступные виды тренировок.
     *
     * @param trainingTypes Множество строк с видами тренировок.
     */
    public void viewTrainingTypes(Set<String> trainingTypes) {
        displayMessage("Виды тренировок: ");
        for (String type : trainingTypes) {
            displayMessage(type);
        }
    }

    /**
     * Отображает статистику по тренировкам.
     *
     * @param totalCaloriesBurned Общее количество сожженных калорий за выбранный период.
     */
    public void displayStatistic(int totalCaloriesBurned) {
        System.out.println("Сожжено калорий за выбранный период: " + totalCaloriesBurned);
    }

    /**
     * Отображает переданное сообщение.
     *
     * @param message Сообщение для отображения.
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Отображает сообщение о некорректных данных.
     */
    public void displayUnknownDataMessage() {
        System.out.println("Неизвестные данные, попробуйте снова");
    }

}


