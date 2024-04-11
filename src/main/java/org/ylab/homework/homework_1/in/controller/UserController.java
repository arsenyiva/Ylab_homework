package org.ylab.homework.homework_1.in.controller;

import org.ylab.homework.homework_1.model.Role;
import org.ylab.homework.homework_1.model.Training;
import org.ylab.homework.homework_1.model.User;
import org.ylab.homework.homework_1.service.UserService;

import java.util.List;

/**
 * Класс для управления пользователями в системе.
 */
public class UserController {
    private UserService userService;

    /**
     * Конструктор для создания контроллера пользователя с указанным сервисом пользователей.
     *
     * @param userService Сервис пользователей, с которым будет работать контроллер
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Регистрирует нового пользователя с указанными данными.
     *
     * @param username Имя пользователя
     * @param password Пароль пользователя
     * @param role Роль пользователя
     * @param trainings Список тренировок пользователя
     */
    public void registerUser(String username, String password, Role role, List<Training> trainings) {
        int id = userService.generateId();
        User user = new User(id, username, password, role, trainings);
        userService.register(user);
        System.out.println("Регистрация прошла успешно!");
    }

    /**
     * Позволяет пользователю войти в систему с указанным именем пользователя и паролем.
     *
     * @param username Имя пользователя
     * @param password Пароль пользователя
     * @return Возвращает объект пользователя, если вход прошел успешно, иначе возвращает null
     */
    public User loginUser(String username, String password) {
        User user = userService.login(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Вход прошел успешно");
            return user;
        } else {
            System.out.println("Неверное имя пользователя или пароль");
        }
        return null;
    }

    /**
     * Обновляет роль пользователя с указанным именем.
     *
     * @param username Имя пользователя, роль которого нужно обновить
     * @param newRole Новая роль пользователя
     */
    public void updateUserRole(String username, Role newRole) {
        User user = userService.getUser(username);
        if (user != null) {
            userService.updateUserRole(username, newRole);
            System.out.println("Роль пользователя успешно изменена.");
        } else {
            System.out.println("Пользователь с таким именем не найден.");
        }
    }
}
