package org.ylab.homework.homework_1.out;

import org.ylab.homework.homework_1.in.controller.TrainingController;
import org.ylab.homework.homework_1.in.controller.UserController;
import org.ylab.homework.homework_1.model.Role;
import org.ylab.homework.homework_1.model.Training;
import org.ylab.homework.homework_1.model.User;
import org.ylab.homework.homework_1.util.UserAuditLogger;

import java.time.LocalDate;
import java.util.*;

/**
 * Консольный интерфейс для взаимодействия с приложением.
 */
public class ConsoleUI {
    private final UserController userController;
    private final TrainingController trainingController;
    private final Scanner scanner;


    /**
     * Конструктор для инициализации консольного интерфейса.
     *
     * @param userController контроллер пользователей
     * @param trainingController контроллер тренировок
     */
    public ConsoleUI(UserController userController, TrainingController trainingController) {
        this.userController = userController;
        this.trainingController = trainingController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Начать выполнение программы.
     */
    public void start() {
        System.out.println("Добро пожаловать в приложение!");

        boolean running = true;
        while (running) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Зарегистрироваться");
            System.out.println("2. Войти");
            System.out.println("3. Выключить приложение");
            System.out.print("Введите цифру: ");
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
                    System.out.println("\nДо свидания!");
                    running = false;
                    break;
                default:
                    System.out.println("Неизвестные данные, попробуйте снова");
            }
        }
    }

    /**
     * Регистрация нового пользователя.
     */
    private void registerUser() {
        System.out.print("Введите имя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        List<Training> trainings = new ArrayList<>();
        userController.registerUser(username, password, Role.USER, trainings);
        UserAuditLogger.logUserRegistration(username);
    }

    /**
     * Вход пользователя в систему.
     */
    private void loginUser() {
        System.out.print("Введите имя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
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
            System.out.println("Пользователь с таким именем и паролем не найден.");
        }
    }

    /**
     * Метод для взаимодействия пользователя с меню тренировок.
     * @param user Пользователь, совершающий действия.
     */
    private void interactWithTraining(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nМеню тренировок: ");
            System.out.println("1. Добавить тренировку ");
            System.out.println("2. Просмотр моих тренировок ");
            System.out.println("3. Удалить тренировку ");
            System.out.println("4. Редактировать тренировку ");
            System.out.println("5. Получить статистику по тренировкам ");
            System.out.println("6. Выйти из аккаунта ");
            System.out.print("Введите цифру: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addTraining(user);
                    break;
                case 2:
                    viewTrainings(user);
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
                    System.out.println("Неизвестные данные, попробуйте снова");
            }
        }
    }

    /**
     * Метод для отображения панели администратора и выполнения административных действий.
     *
     * @param user Админ, совершающий действия.
     */
    private void adminPanel(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nПанель администратора: ");
            System.out.println("1. Просмотр всех тренировок: ");
            System.out.println("2. Сменить роль пользователя: ");
            System.out.println("3. Просмотр действий пользователей: ");
            System.out.println("4. Выйти из аккаунта ");
            System.out.print("Введите цифру: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewTrainings(user);
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
                    System.out.println("Неизвестные данные, попробуйте снова");
            }
        }
    }

    /**
     * Отображает тренировки
     * @param user Пользователь, чьи тренировки нужно отобразить.
     * Если пользователь админ - то показывает все тренировки
     */
    public void viewTrainings(User user) {
        List<Training> trainings;
        if (user.getRole() == Role.ADMIN) {
            // Если пользователь - администратор, получаем все тренировки
            trainings = trainingController.getAllTrainings();
        } else {
            trainings = trainingController.getTrainings(user);
        }

        if (trainings.isEmpty()) {
            System.out.println("Тренировок не найдено...");
        } else {
            trainings.sort(Comparator.comparing(Training::getDate));
            System.out.println("Тренировки:");
            for (Training training : trainings) {
                displayTraining(training);
            }
        }
    }

    /**
     * Добавляет новую тренировку для указанного пользователя.
     *
     * @param user Пользователь, для которого добавляется тренировка.
     */
    private void addTraining(User user) {
        Scanner scanner = new Scanner(System.in);

        Set<String> trainingTypes = trainingController.getTrainingTypes();
        viewTrainingTypes(trainingTypes);

        System.out.print("Выберите вид тренировки выше или добавьте новый : ");
        String typeName = scanner.nextLine();


        System.out.print("Введите дату тренировки (гггг-мм-дд): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        List<Training> trainings = trainingController.getTrainings(user);
        boolean trainingExists = trainings.stream()
                .anyMatch(training -> training.getDate()
                        .equals(date) && training.getType()
                        .getName().equalsIgnoreCase(typeName));

        if (trainingExists) {
            System.out.println("Тренировка данного типа на указанную дату уже существует.");
        } else {
            System.out.print("Введите продолжительность в минутах: ");
            int duration = scanner.nextInt();
            System.out.print("Введите сожженные калории: ");
            int calories = scanner.nextInt();
            System.out.print("Введите дополнительную информацию (Расстояние / количество подходов и т.д.: ");
            scanner.nextLine();
            String info = scanner.nextLine();

            trainingController.addTraining(user, typeName, date, duration, calories, info);
            UserAuditLogger.logTrainingAdded(user.getUsername(), date, typeName);
        }
    }

    /**
     * Удаляет тренировку для указанного пользователя по дате и типу тренировки.
     * @param user Пользователь, для которого удаляется тренировка.
     */
    private void deleteTraining(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите дату тренировки в формате (yyyy-MM-dd): ");
        String dateString = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateString);

        System.out.print("Введите вид тренировки: ");
        String type = scanner.nextLine();

        trainingController.deleteTraining(user, date, type);
        UserAuditLogger.logTrainingDeleted(user.getUsername(), date, type);

    }

    /**
     * Редактирует тренировку для указанного пользователя по дате и типу тренировки.
     * @param user Пользователь, для которого редактируется тренировка.
     */
    private void editTraining(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите дату тренировки (гггг-мм-дд): ");
        String dateString = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateString);

        System.out.print("Введите тип тренировки: ");
        String type = scanner.nextLine();

        Set<String> trainingTypes = trainingController.getTrainingTypes();
        viewTrainingTypes(trainingTypes);

        System.out.print("Введите новый тип тренировки: ");
        String newType = scanner.nextLine();

        System.out.print("Введите новую продолжительность тренировки в минутах: ");
        int newDuration = scanner.nextInt();

        System.out.print("Введите новое количество сожженных калорий: ");
        int newCalories = scanner.nextInt();

        System.out.print("Введите новое описание тренировки: ");
        scanner.nextLine();
        String newInfo = scanner.nextLine();

        trainingController.editTraining(user, date, type, newType, newDuration, newCalories, newInfo);
        UserAuditLogger.logTrainingEdited(user.getUsername(), date, type);
    }

    /**
     * Отображает доступные виды тренировок.
     * @param trainingTypes Множество доступных видов тренировок.
     */
    private void viewTrainingTypes(Set<String> trainingTypes) {
        System.out.println("Виды тренировок: ");
        for (String type : trainingTypes) {
            System.out.println(type);
        }
    }

    /**
     * Выводит информацию о тренировке на консоль.
     * @param training Тренировка, информацию о которой нужно вывести.
     */
    public void displayTraining(Training training) {
        System.out.println("Имя: " + training.getUser().getUsername());
        System.out.println("Дата: " + training.getDate());
        System.out.println("Вид: " + training.getType().getName());
        System.out.println("Время: " + training.getDurationMinutes() + " минут");
        System.out.println("Калорий сожжено: " + training.getCaloriesBurned());
        System.out.println("Доп инфо: " + training.getAdditionalInfo());
        System.out.println();
    }

    /**
     * Выводит статистику тренировок пользователя за определенный период.
     * @param user Пользователь, чьи тренировки нужно учитывать.
     */
    public void viewStatistic(User user) {
        List<Training> trainings = trainingController.getTrainings(user);
        if (trainings.isEmpty()) {
            System.out.println("Тренировок не найдено...");
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите дату начала периода (гггг-мм-дд): ");
            String startDateString = scanner.nextLine();
            LocalDate startDate = LocalDate.parse(startDateString);
            System.out.print("Введите дату конца периода (гггг-мм-дд): ");
            String endDateString = scanner.nextLine();
            LocalDate endDate = LocalDate.parse(endDateString);
            int totalCaloriesBurned = trainingController.getTotalCaloriesBurned(user, startDate, endDate);
            System.out.println("Сожжено калорий за выбранный период: " + totalCaloriesBurned);
        }
    }

    /**
     * Изменяет роль пользователя.
     */
    public void changeUserRole() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите роль пользователя (USER, ADMIN): ");
        String roleInput = scanner.nextLine();
        Role newRole = Role.valueOf(roleInput.toUpperCase());
        userController.updateUserRole(username, newRole);
    }
}
