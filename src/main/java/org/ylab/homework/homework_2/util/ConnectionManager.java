package org.ylab.homework.homework_2.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import java.io.IOException;
import java.io.InputStream;

/**
 * Менеджер соединений с базой данных.
 */
public class ConnectionManager {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();
    private static Connection connection;

    /**
     Блок инициализации статических переменных
     */
    static {
        try {
            InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (inputStream == null) {
                throw new RuntimeException("Unable to load configuration file: " + CONFIG_FILE);
            }
            properties.load(inputStream);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            connection = DriverManager.getConnection(url, user, password);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    /**
     * Получает соединение с базой данных.
     *
     * @return Соединение с базой данных
     */
    public static Connection getConnection() {
        return connection;
    }
}