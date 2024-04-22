package org.ylab.homework.homework_2.main;

import liquibase.exception.DatabaseException;
import org.ylab.homework.homework_2.controller.TrainingController;
import org.ylab.homework.homework_2.controller.UserController;
import org.ylab.homework.homework_2.database.TrainingRepository;
import org.ylab.homework.homework_2.database.TrainingTypeRepository;
import org.ylab.homework.homework_2.database.UserRepository;
import org.ylab.homework.homework_2.service.TrainingService;
import org.ylab.homework.homework_2.service.TrainingTypeService;
import org.ylab.homework.homework_2.service.UserService;
import org.ylab.homework.homework_2.out.OutputHandler;
import org.ylab.homework.homework_2.in.InputHandler;
import org.ylab.homework.homework_2.util.ConnectionManager;
import org.ylab.homework.homework_2.util.LiquibaseUtil;
import org.ylab.homework.homework_2.util.UserAuditLogger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Главный класс приложения.
 * Содержит метод main для запуска приложения.
 */
public class TrainingDiaryApp {
    public static void main(String[] args) throws SQLException, IOException, DatabaseException {
        Connection connection = ConnectionManager.getConnection();
        LiquibaseUtil liquibaseUtil = new LiquibaseUtil();
        liquibaseUtil.executeMigration();
        UserAuditLogger userAuditLogger = new UserAuditLogger(connection);
        UserRepository userRepository = new UserRepository(connection);
        TrainingRepository trainingRepository = new TrainingRepository(connection);
        TrainingTypeRepository trainingTypeRepository = new TrainingTypeRepository(connection);
        UserService userService = new UserService(userRepository);
        TrainingTypeService trainingTypeService = new TrainingTypeService(trainingTypeRepository);
        TrainingService trainingService = new TrainingService(trainingRepository);

        UserController userController = new UserController(userService);
        TrainingController trainingController = new TrainingController(trainingService, trainingTypeService);

        OutputHandler outputHandler = new OutputHandler();
        InputHandler inputHandler = new InputHandler(userController, trainingController, outputHandler);

        inputHandler.handleInput();
    }
}
