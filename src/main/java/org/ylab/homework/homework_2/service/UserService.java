package org.ylab.homework.homework_2.service;

import org.ylab.homework.homework_2.database.UserRepository;
import org.ylab.homework.homework_2.model.Role;
import org.ylab.homework.homework_2.model.User;

import java.sql.SQLException;
import java.util.List;


/**
 * Сервис для работы с пользователями.
 */
public class UserService {
    private UserRepository userRepository;

    /**
     * Создает новый экземпляр UserService с указанной базой данных пользователей.
     * @param userRepository база данных пользователей
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Регистрирует нового пользователя.
     * @param user новый пользователь
     */
    public void register(User user) throws SQLException {
        userRepository.addUser(user);
    }

    /**
     * Авторизует пользователя по имени пользователя.
     * @param username имя пользователя для входа
     * @return объект пользователя, если пользователь существует, иначе null
     */
    public User login(String username) throws SQLException {
        return userRepository.getUser(username);
    }

    /**
     * Обновляет роль пользователя.
     * @param username имя пользователя
     * @param newRole новая роль пользователя
     */
    public void updateUserRole(String username, Role newRole) throws SQLException {
        userRepository.updateUserRole(username, newRole);
    }

    /**
     * Получает всех пользователей.
     * @return список всех пользователей
     */
    public List<User> getAllUsers() throws SQLException {
        return userRepository.getAllUsers();
    }

    /**
     * Получает пользователя по имени пользователя.
     * @param username имя пользователя
     * @return объект пользователя, если пользователь существует, иначе null
     */
    public User getUser(String username) throws SQLException {
        return userRepository.getUser(username);
    }

    /**
     * Генерирует уникальный идентификатор пользователя.
     * @return уникальный идентификатор пользователя
     */

}
