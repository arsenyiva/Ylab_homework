package org.ylab.homework.homework_1.in;

import org.ylab.homework.homework_1.controller.TrainingController;
import org.ylab.homework.homework_1.controller.UserController;
import org.ylab.homework.homework_1.model.Role;
import org.ylab.homework.homework_1.model.Training;
import org.ylab.homework.homework_1.model.User;
import org.ylab.homework.homework_1.out.OutputHandler;
import org.ylab.homework.homework_1.util.UserAuditLogger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Класс InputHandler обрабатывает ввод пользователя и взаимодействует с контроллерами приложения и обработчиком вывода.
 */
public class InputHandler {
    private final UserController userController;
    private final TrainingController trainingController;
    private final OutputHandler outputHandler;
    private final Scanner scanner;

    public InputHandler(UserController userController, TrainingController trainingController, OutputHandler outputHandler) {
        this.userController = userController;
        this.trainingController = trainingController;
        this.outputHandler = outputHandler;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Обрабатывает ввод пользователя и управляет основным потоком выполнения приложения.
     */
    public void handleInput() {
        outputHandler.displayMessage("Добро пожаловать в приложение!");
        boolean running = true;
        while (running) {
            outputHandler.displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    outputHandler.displayMessage("\nДо свидания!");
                    running = false;
                    break;
                default:
                    outputHandler.displayMessage("Неизвестные данные, попробуйте снова");
            }
        }
    }

    /**
     * Регистрирует нового пользователя в системе.
     */
    private void registerUser() {
        outputHandler.displayMessage("Введите имя: ");
        String username = scanner.nextLine();
        outputHandler.displayMessage("Введите пароль: ");
        String password = scanner.nextLine();
        List<Training> trainings = new ArrayList<>();
        userController.registerUser(username, password, Role.USER, trainings);
        UserAuditLogger.logUserRegistration(username);
        outputHandler.displayMessage("Пользователь успешно зарегистрирован.");
    }

    /**
     * Выполняет процесс входа зарегистрированного пользователя в систему.
     */

    private void loginUser() {
        outputHandler.displayMessage("Введите имя: ");
        String username = scanner.nextLine();
        outputHandler.displayMessage("Введите пароль: ");
        String password = scanner.nextLine();
        User user = userController.loginUser(username, password);

        if (user != null) {
            if (user.getRole() == Role.ADMIN) {
                UserAuditLogger.logUserLoggedIn(username);
                adminPanel(user);
            } else {
                UserAuditLogger.logUserLoggedIn(username);
                interactWithTraining(user);
            }
        } else {
            UserAuditLogger.logFailedLoginAttempt(username);
            outputHandler.displayMessage("Пользователь с таким именем и паролем не найден.");
        }
    }

    /**
     * Обеспечивает взаимодействие пользователя с тренировками.
     *
     * @param user Пользователь, совершающий действия.
     */
    private void interactWithTraining(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            outputHandler.displayUserMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addTraining(user);
                    break;
                case 2:
                    outputHandler.displayTrainings(user.getTrainings());
                    break;
                case 3:
                    deleteTraining(user);
                    break;
                case 4:
                    editTraining(user);
                    break;
                case 5:
                    viewStatistic(user);
                    break;
                case 6:
                    loggedIn = false;
                    UserAuditLogger.logLogout(user.getUsername());
                    break;
                default:
                    outputHandler.displayUnknownDataMessage();
            }
        }
    }

    /**
     * Метод для выполнения административных действий.
     *
     * @param user Админ, совершающий действия.
     */
    private void adminPanel(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            outputHandler.displayAdminMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    outputHandler.displayTrainings(user.getRole() == Role.ADMIN ? trainingController.getAllTrainings() : trainingController.getTrainings(user));
                    break;
                case 2:
                    changeUserRole();
                    break;
                case 3:
                    UserAuditLogger.printAuditLog();
                    UserAuditLogger.logLogout(user.getUsername());
                    break;
                case 4:
                    loggedIn = false;
                    break;
                default:
                    outputHandler.displayUnknownDataMessage();
            }
        }
    }

    /**
     * Позволяет получить статистику тренировок пользователя.
     *
     * @param user Пользователь, совершающий действия.
     */
    private void viewStatistic(User user) {
        List<Training> trainings = trainingController.getTrainings(user);
        if (trainings.isEmpty()) {
            outputHandler.displayMessage("Тренировок не найдено...");
        } else {
            outputHandler.displayMessage("Введите дату начала периода (гггг-мм-дд): ");
            String startDateString = scanner.nextLine();
            LocalDate startDate = LocalDate.parse(startDateString);
            outputHandler.displayMessage("Введите дату конца периода (гггг-мм-дд): ");
            String endDateString = scanner.nextLine();
            LocalDate endDate = LocalDate.parse(endDateString);
            int totalCaloriesBurned = trainingController.getTotalCaloriesBurned(user, startDate, endDate);
            outputHandler.displayStatistic(totalCaloriesBurned);
        }
    }

    /**
     * Позволяет пользователю редактировать данные о тренировке.
     *
     * @param user Пользователь, совершающий действия.
     */
    private void editTraining(User user) {
        outputHandler.displayMessage("Введите дату тренировки (гггг-мм-дд): ");
        String dateString = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateString);

        outputHandler.displayMessage("Введите тип тренировки: ");
        String type = scanner.nextLine();

        Set<String> trainingTypes = trainingController.getTrainingTypes();
        outputHandler.viewTrainingTypes(trainingTypes);

        outputHandler.displayMessage("Введите новый тип тренировки: ");
        String newType = scanner.nextLine();

        outputHandler.displayMessage("Введите новую продолжительность тренировки в минутах: ");
        int newDuration = scanner.nextInt();

        outputHandler.displayMessage("Введите новое количество сожженных калорий: ");
        int newCalories = scanner.nextInt();

        outputHandler.displayMessage("Введите новое описание тренировки: ");
        scanner.nextLine();
        String newInfo = scanner.nextLine();

        trainingController.editTraining(user, date, type, newType, newDuration, newCalories, newInfo);
        UserAuditLogger.logTrainingEdited(user.getUsername(), date, type);
    }

    /**
     * Позволяет пользователю добавить новую тренировку.
     *
     * @param user Пользователь, совершающий действия.
     */
    private void addTraining(User user) {
        Set<String> trainingTypes = trainingController.getTrainingTypes();
        outputHandler.viewTrainingTypes(trainingTypes);

        outputHandler.displayMessage("Выберите вид тренировки выше или добавьте новый : ");
        String typeName = scanner.nextLine();

        outputHandler.displayMessage("Введите дату тренировки (гггг-мм-дд): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        List<Training> trainings = trainingController.getTrainings(user);
        boolean trainingExists = trainings.stream()
                .anyMatch(training -> training.getDate()
                        .equals(date) && training.getType()
                        .getName().equalsIgnoreCase(typeName));

        if (trainingExists) {
            outputHandler.displayMessage("Тренировка данного типа на указанную дату уже существует.");
        } else {
            outputHandler.displayMessage("Введите продолжительность в минутах: ");
            int duration = scanner.nextInt();
            outputHandler.displayMessage("Введите сожженные калории: ");
            int calories = scanner.nextInt();
            outputHandler.displayMessage("Введите дополнительную информацию (Расстояние / количество подходов и т.д.: ");
            scanner.nextLine();
            String info = scanner.nextLine();

            trainingController.addTraining(user, typeName, date, duration, calories, info);
            UserAuditLogger.logTrainingAdded(user.getUsername(), date, typeName);
        }
    }

    /**
     * Позволяет пользователю удалить тренировку.
     *
     * @param user Пользователь, совершающий действия.
     */
    private void deleteTraining(User user) {
        outputHandler.displayMessage("Введите дату тренировки в формате (yyyy-MM-dd): ");
        String dateString = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateString);

        outputHandler.displayMessage("Введите вид тренировки: ");
        String type = scanner.nextLine();

        trainingController.deleteTraining(user, date, type);
        UserAuditLogger.logTrainingDeleted(user.getUsername(), date, type);
    }
    /**
     * Позволяет админу изменить роль пользователя.
     */
    private void changeUserRole() {
        outputHandler.displayMessage("Введите имя пользователя: ");
        String username = scanner.nextLine();
        outputHandler.displayMessage("Введите роль пользователя (USER, ADMIN): ");
        String roleInput = scanner.nextLine();
        Role newRole = Role.valueOf(roleInput.toUpperCase());
        userController.updateUserRole(username, newRole);
    }
}

