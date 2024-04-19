package org.ylab.homework.homework_2.database;

import org.ylab.homework.homework_2.model.TrainingType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Репозиторий для работы с типами тренировок в базе данных.
 */
public class TrainingTypeRepository {
    private Connection connection;

    /**
     * Конструктор класса TrainingTypeRepository.
     *
     * @param connection соединение с базой данных
     */
    public TrainingTypeRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Добавляет новый тип тренировки в базу данных.
     *
     * @param trainingType новый тип тренировки
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public void addTrainingType(TrainingType trainingType) throws SQLException {
        String query = "INSERT INTO  training_app.training_types (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, trainingType.getName());
            statement.executeUpdate();
        }
    }

    /**
     * Получает список всех типов тренировок из базы данных.
     *
     * @return Список всех типов тренировок из базы данных
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public List<TrainingType> getAllTrainingTypes() throws SQLException {
        List<TrainingType> trainingTypes = new ArrayList<>();
        String query = "SELECT * FROM  training_app.training_types";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");
                TrainingType trainingType = new TrainingType(name, id);
                trainingTypes.add(trainingType);
            }
        }
        return trainingTypes;
    }

    /**
     * Проверяет, содержится ли указанный тип тренировки в базе данных.
     *
     * @param trainingType тип тренировки для проверки
     * @return true, если тип тренировки содержится в базе данных, в противном случае - false
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public boolean containsTrainingType(String trainingType) throws SQLException {
        String query = "SELECT COUNT(*) FROM  training_app.training_types WHERE UPPER(name) = UPPER(?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, trainingType);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    /**
     * Получает идентификатор типа тренировки по его имени.
     *
     * @param typeName имя типа тренировки
     * @return Идентификатор типа тренировки, или -1, если тип тренировки не найден
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public int getTrainingTypeId(String typeName) throws SQLException {
        int typeId = -1;
        String query = "SELECT id FROM training_app.training_types WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, typeName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    typeId = resultSet.getInt("id");
                }
            }
        }

        return typeId;
    }

}
