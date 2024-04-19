package org.ylab.homework.homework_2.database;

import org.ylab.homework.homework_2.model.Role;
import org.ylab.homework.homework_2.model.Training;
import org.ylab.homework.homework_2.model.TrainingType;
import org.ylab.homework.homework_2.model.User;

import java.time.LocalDate;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с тренировками пользователей в базе данных.
 */
public class TrainingRepository {
    private Connection connection;

    /**
     * Конструктор класса TrainingRepository.
     *
     * @param connection соединение с базой данных
     */
    public TrainingRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Добавляет тренировку для указанного пользователя в базу данных.
     *
     * @param user     Пользователь, для которого добавляется тренировка
     * @param training Добавляемая тренировка
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public void addTraining(User user, Training training) throws SQLException {
        String query = "INSERT INTO  training_app.trainings (user_id, date, type_id, duration_minutes, calories_burned, additional_info) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            statement.setDate(2, java.sql.Date.valueOf(training.getDate()));
            statement.setInt(3, training.getType().getId());
            statement.setInt(4, training.getDurationMinutes());
            statement.setInt(5, training.getCaloriesBurned());
            statement.setString(6, training.getAdditionalInfo());
            statement.executeUpdate();
        }
    }

    /**
     * Возвращает список тренировок для указанного пользователя.
     *
     * @param user Пользователь, для которого запрашиваются тренировки
     * @return Список тренировок пользователя, или пустой список, если пользователь не имеет тренировок
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public List<Training> getTrainings(User user) throws SQLException {
        List<Training> trainings = new ArrayList<>();
        String query = "SELECT * FROM  training_app.trainings WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    LocalDate date = resultSet.getDate("date").toLocalDate();
                    int typeId = resultSet.getInt("type_id");
                    int durationMinutes = resultSet.getInt("duration_minutes");
                    int caloriesBurned = resultSet.getInt("calories_burned");
                    String additionalInfo = resultSet.getString("additional_info");
                    TrainingType type = getTrainingTypeById(typeId);
                    Training training = new Training(id, date, type, durationMinutes, caloriesBurned, additionalInfo, user);
                    trainings.add(training);
                }
            }
        }
        return trainings;
    }


    /**
     * Обновляет указанную тренировку для указанного пользователя в базе данных.
     *
     * @param user        Пользователь, чья тренировка должна быть обновлена
     * @param oldTraining Старая версия тренировки
     * @param newTraining Новая версия тренировки
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public void updateTraining(User user, Training oldTraining, Training newTraining) throws SQLException {
        String query = "UPDATE  training_app.trainings SET date = ?, type_id = ?, duration_minutes = ?, calories_burned = ?, additional_info = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, java.sql.Date.valueOf(newTraining.getDate()));
            statement.setInt(2, newTraining.getType().getId());
            statement.setInt(3, newTraining.getDurationMinutes());
            statement.setInt(4, newTraining.getCaloriesBurned());
            statement.setString(5, newTraining.getAdditionalInfo());
            statement.setInt(6, oldTraining.getId());
            statement.setInt(7, user.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Удаляет указанную тренировку для указанного пользователя из базы данных.
     *
     * @param user     Пользователь, чья тренировка должна быть удалена
     * @param training Тренировка, которая должна быть удалена
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public void deleteTraining(User user, Training training) throws SQLException {
        String query = "DELETE FROM  training_app.trainings WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, training.getId());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Получает список всех тренировок из базы данных.
     *
     * @return Список всех тренировок из базы данных
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public List<Training> getAllTrainings() throws SQLException {
        List<Training> allTrainings = new ArrayList<>();
        String query = "SELECT * FROM  training_app.trainings";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                int typeId = resultSet.getInt("type_id");
                int durationMinutes = resultSet.getInt("duration_minutes");
                int caloriesBurned = resultSet.getInt("calories_burned");
                String additionalInfo = resultSet.getString("additional_info");

                User user = getUserById(userId);
                TrainingType type = getTrainingTypeById(typeId);

                Training training = new Training(id, date, type, durationMinutes, caloriesBurned, additionalInfo, user);
                allTrainings.add(training);
            }
        }
        return allTrainings;
    }

    /**
     * Получает тип тренировки по его идентификатору.
     *
     * @param typeId Идентификатор типа тренировки
     * @return Тип тренировки
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    private TrainingType getTrainingTypeById(int typeId) throws SQLException {
        String query = "SELECT * FROM  training_app.training_types WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, typeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    return new TrainingType(name, typeId);
                }
            }
        }
        return null;
    }

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя
     * @return Пользователь
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    private User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM  training_app.users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    Role role = Role.valueOf(resultSet.getString("role"));
                    return new User(userId, username, password, role, new ArrayList<>());
                }
            }
        }
        return null;
    }
}

