package org.ylab.homework.homework_2.database;

import org.ylab.homework.homework_2.model.Role;
import org.ylab.homework.homework_2.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с данными пользователей в базе данных.
 */
public class UserRepository {
    private Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param user Новый пользователь
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO  training_app.users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Failed to get generated user ID.");
            }
        }
    }

    /**
     * Возвращает пользователя по его имени пользователя.
     *
     * @param username Имя пользователя
     * @return Объект пользователя, или null, если пользователь не найден
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public User getUser(String username) throws SQLException {
        String query = "SELECT * FROM training_app.users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String password = resultSet.getString("password");
                    String roleStr = resultSet.getString("role");
                    Role role = Role.valueOf(roleStr);
                    return new User(id, username, password, role, null);
                }
            }
        }
        return null;
    }

    /**
     * Обновляет роль пользователя.
     *
     * @param username Имя пользователя, чья роль должна быть обновлена
     * @param newRole  Новая роль пользователя
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public void updateUserRole(String username, Role newRole) throws SQLException {
        String query = "UPDATE  training_app.users SET role = ? WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newRole.toString());
            statement.setString(2, username);
            statement.executeUpdate();
        }
    }

    /**
     * Возвращает список всех пользователей в базе данных.
     *
     * @return Список всех пользователей
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM  training_app.users";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String roleStr = resultSet.getString("role");
                Role role = Role.valueOf(roleStr);
                userList.add(new User(id, username, password, role, null));
            }
        }
        return userList;
    }
}
