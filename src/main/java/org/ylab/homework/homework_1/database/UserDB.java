package org.ylab.homework.homework_1.database;

import org.ylab.homework.homework_1.model.Role;
import org.ylab.homework.homework_1.model.User;

import java.util.*;


/**
 * Класс для работы с базой данных пользователей.
 */
public class UserDB {
    private Map<String, User> users = new HashMap<>();

    /**
     * Добавляет нового пользователя в базу данных.
     * @param user Новый пользователь
     */
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    /**
     * Возвращает пользователя по его имени пользователя.
     * @param username Имя пользователя
     * @return Объект пользователя, или null, если пользователь не найден
     */
    public User getUser(String username) {
        return users.get(username);
    }

    /**
     * Обновляет роль пользователя.
     * @param username Имя пользователя, чья роль должна быть обновлена
     * @param newRole Новая роль пользователя
     */
    public void updateUserRole(String username, Role newRole) {
        User user = users.get(username);
        user.setRole(newRole);
    }

    /**
     * Возвращает список всех пользователей в базе данных.
     * @return Список всех пользователей
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
