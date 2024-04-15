package org.ylab.homework.homework_1.service;

import org.ylab.homework.homework_1.database.UserDB;
import org.ylab.homework.homework_1.model.Role;
import org.ylab.homework.homework_1.model.User;

import java.util.List;
import java.util.Random;


/**
 * Сервис для работы с пользователями.
 */
public class UserService {
    private UserDB userDB;

    /**
     * Создает новый экземпляр UserService с указанной базой данных пользователей.
     * @param userDB база данных пользователей
     */
    public UserService(UserDB userDB) {
        this.userDB = userDB;
    }

    /**
     * Регистрирует нового пользователя.
     * @param user новый пользователь
     */
    public void register(User user) {
        userDB.addUser(user);
    }

    /**
     * Авторизует пользователя по имени пользователя.
     * @param username имя пользователя для входа
     * @return объект пользователя, если пользователь существует, иначе null
     */
    public User login(String username) {
        return userDB.getUser(username);
    }

    /**
     * Обновляет роль пользователя.
     * @param username имя пользователя
     * @param newRole новая роль пользователя
     */
    public void updateUserRole(String username, Role newRole) {
        userDB.updateUserRole(username, newRole);
    }

    /**
     * Получает всех пользователей.
     * @return список всех пользователей
     */
    public List<User> getAllUsers() {
        return userDB.getAllUsers();
    }

    /**
     * Получает пользователя по имени пользователя.
     * @param username имя пользователя
     * @return объект пользователя, если пользователь существует, иначе null
     */
    public User getUser(String username) {
        return userDB.getUser(username);
    }

    /**
     * Генерирует уникальный идентификатор пользователя.
     * @return уникальный идентификатор пользователя
     */
    public int generateId() {
        Random random = new Random();
        return random.nextInt(10000);
    }
}
