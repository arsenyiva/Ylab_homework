package org.ylab.homework.homework_1.util;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



/**
 * Класс для логирования действий пользователей.
 */
public class UserAuditLogger {
    public static List<String> auditLog = new ArrayList<>();

    /**
     * Логирует регистрацию пользователя.
     *
     * @param username имя пользователя
     */
    public static void logUserRegistration(String username) {
        String message = "Пользователь " + username + " зарегистрировался " + LocalDateTime.now();
        auditLog.add(message);
    }

    /**
     * Логирует вход пользователя.
     *
     * @param username имя пользователя
     */
    public static void logUserLoggedIn(String username) {
        String message = "Пользователь " + username + " произвел вход в приложение в " + LocalDateTime.now();
        auditLog.add(message);
    }

    /**
     * Логирует добавление тренировки пользователем.
     *
     * @param username имя пользователя
     * @param date дата тренировки
     * @param type тип тренировки
     */
    public static void logTrainingAdded(String username, LocalDate date, String type) {
        String message = "Пользователь " + username + " добавил тренировку типа - " + type + " " + date + " в " + LocalDateTime.now();
        auditLog.add(message);
    }

    /**
     * Логирует удаление тренировки пользователем.
     *
     * @param username имя пользователя
     * @param date дата тренировки
     * @param type тип тренировки
     */
    public static void logTrainingDeleted(String username, LocalDate date, String type) {
        String message = "Пользователь " + username + " удалил тренировку " + type + " " + date + " в " + LocalDateTime.now();
        auditLog.add(message);
    }

    /**
     * Логирует редактирование тренировки пользователем.
     * @param username имя пользователя
     * @param date дата тренировки
     * @param type тип тренировки
     */
    public static void logTrainingEdited(String username, LocalDate date, String type) {
        String message = "Пользователь " + username + " отредактировал тренировку " + type + " " + date + " в " + LocalDateTime.now();
        auditLog.add(message);
    }

    /**
     * Логирует неудачную попытку входа пользователя.
     * @param username имя пользователя
     */
    public static void logFailedLoginAttempt(String username) {
        String message = "Пользователь " + username + " пытался произвести вход в приложение в " + LocalDateTime.now();
        auditLog.add(message);
    }

    /**
     * Логирует выход пользователя.
     * @param username имя пользователя
     */
    public static void logLogout(String username) {
        String message = "Пользователь " + username + " вышел из приложения в " + LocalDateTime.now();
        auditLog.add(message);
    }

    /**
     * Возвращает журнал действий пользователей.
     * @return список записей журнала действий пользователей
     */
    public static List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    /**
     * Выводит журнал действий пользователей в консоль.
     */
    public static void printAuditLog() {
        List<String> auditLog = getAuditLog();
        System.out.println("Журнал действий пользователей:");

        for (String message : auditLog) {
            System.out.println(message);
        }
    }
}
