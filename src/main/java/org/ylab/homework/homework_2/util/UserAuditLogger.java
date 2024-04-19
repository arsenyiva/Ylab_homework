package org.ylab.homework.homework_2.util;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Класс для логирования действий пользователей.
 */
public class UserAuditLogger {
    public static final String INSERT_SQL = "INSERT INTO  training_app.audit_log (message, created_at) VALUES (?, ?)";
    private static Connection connection;


    public UserAuditLogger(Connection connection) {
        this.connection = connection;
    }


    /**
     * Логирует регистрацию пользователя.
     *
     * @param username имя пользователя
     */
    public static void logUserRegistration(String username) throws SQLException {
        String message = "Пользователь " + username + " зарегистрировался " + LocalDateTime.now();
        saveToDatabase(message);
    }

    /**
     * Логирует вход пользователя.
     *
     * @param username имя пользователя
     */
    public static void logUserLoggedIn(String username) throws SQLException {
        String message = "Пользователь " + username + " произвел вход в приложение в " + LocalDateTime.now();
        saveToDatabase(message);
    }

    /**
     * Логирует добавление тренировки пользователем.
     *
     * @param username имя пользователя
     * @param date     дата тренировки
     * @param type     тип тренировки
     */
    public static void logTrainingAdded(String username, LocalDate date, String type) throws SQLException {
        String message = "Пользователь " + username + " добавил тренировку типа - " + type + " " + date + " в " + LocalDateTime.now();
        saveToDatabase(message);
    }

    /**
     * Логирует удаление тренировки пользователем.
     *
     * @param username имя пользователя
     * @param date     дата тренировки
     * @param type     тип тренировки
     */
    public static void logTrainingDeleted(String username, LocalDate date, String type) throws SQLException {
        String message = "Пользователь " + username + " удалил тренировку " + type + " " + date + " в " + LocalDateTime.now();
        saveToDatabase(message);
    }

    /**
     * Логирует редактирование тренировки пользователем.
     *
     * @param username имя пользователя
     * @param date     дата тренировки
     * @param type     тип тренировки
     */
    public static void logTrainingEdited(String username, LocalDate date, String type) throws SQLException {
        String message = "Пользователь " + username + " отредактировал тренировку " + type + " " + date + " в " + LocalDateTime.now();
        saveToDatabase(message);
    }

    /**
     * Логирует неудачную попытку входа пользователя.
     *
     * @param username имя пользователя
     */
    public static void logFailedLoginAttempt(String username) throws SQLException {
        String message = "Пользователь " + username + " пытался произвести вход в приложение в " + LocalDateTime.now();
        saveToDatabase(message);
    }

    /**
     * Логирует выход пользователя.
     *
     * @param username имя пользователя
     */
    public static void logLogout(String username) throws SQLException {
        String message = "Пользователь " + username + " вышел из приложения в " + LocalDateTime.now();
        saveToDatabase(message);
    }

    /**
     * Возвращает журнал действий пользователей.
     *
     * @return список записей журнала действий пользователей
     */
    public static void printAuditLog() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionManager.getConnection();
            preparedStatement = connection.prepareStatement("SELECT message FROM  training_app.audit_log");
            resultSet = preparedStatement.executeQuery();
            System.out.println("Журнал действий пользователей:");
            while (resultSet.next()) {
                String message = resultSet.getString("message");
                System.out.println(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сохраняет сообщение лога в базу данных.
     *
     * @param message сообщение для сохранения в логе
     */
    public static void saveToDatabase(String message) {
        if (connection == null) {
            throw new IllegalStateException("Connection is not initialized");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            preparedStatement.setString(1, message);
            preparedStatement.setObject(2, LocalDateTime.now());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}