package org.ylab.homework.homework_1.main;

import org.ylab.homework.homework_1.controller.TrainingController;
import org.ylab.homework.homework_1.controller.UserController;
import org.ylab.homework.homework_1.database.TrainingDB;
import org.ylab.homework.homework_1.database.UserDB;
import org.ylab.homework.homework_1.in.InputHandler;
import org.ylab.homework.homework_1.model.Role;
import org.ylab.homework.homework_1.out.OutputHandler;
import org.ylab.homework.homework_1.service.TrainingService;
import org.ylab.homework.homework_1.service.TrainingTypeService;
import org.ylab.homework.homework_1.service.UserService;

import java.util.ArrayList;

/**
 * Главный класс приложения.
 * Содержит метод main для запуска приложения.
 */
public class TrainingDiaryApp {

    public static void main(String[] args) {

        UserDB userDB = new UserDB();
        TrainingDB trainingDB = new TrainingDB();
        UserService userService = new UserService(userDB);
        TrainingTypeService trainingTypeService = new TrainingTypeService();
        TrainingService trainingService = new TrainingService(trainingDB);
        UserController userController = new UserController(userService);
        TrainingController trainingController = new TrainingController(trainingService, trainingTypeService);

        //Добавление первоначальных данных в приложение
        trainingController.addTrainingType("БЕГ");
        trainingController.addTrainingType("ПЛАВАНИЕ");
        trainingController.addTrainingType("КРОССФИТ");
        userController.registerUser("admin", "admin", Role.ADMIN, new ArrayList<>());
        userController.registerUser("user", "user", Role.USER, new ArrayList<>());
        OutputHandler outputHandler = new OutputHandler();
        InputHandler inputHandler = new InputHandler(userController, trainingController,outputHandler);
        inputHandler.handleInput();
    }
}

