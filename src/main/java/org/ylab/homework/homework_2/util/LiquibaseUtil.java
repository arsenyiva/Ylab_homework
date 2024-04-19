package org.ylab.homework.homework_2.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Утилитарный класс для работы с Liquibase.
 */
public class LiquibaseUtil {

    /**
     * Получает соединение с базой данных из файла конфигурации.
     *
     * @return Соединение с базой данных
     * @throws SQLException если возникает ошибка при установлении соединения
     * @throws IOException  если возникает ошибка при чтении файла конфигурации
     */
    public static Connection getConnectionFromProperties() throws SQLException, IOException {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get("src/main/resources/config.properties"))) {
            properties.load(input);
        }

        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }

    /**
     * Точка входа в приложение для выполнения миграции Liquibase.
     *
     * @param args аргументы командной строки
     * @throws DatabaseException если возникает ошибка при работе с базой данных
     * @throws SQLException      если возникает ошибка при работе с базой данных
     * @throws IOException       если возникает ошибка при чтении файла конфигурации
     */

    public static void main(String[] args) throws DatabaseException, SQLException, IOException {
        try {
            Connection connection = getConnectionFromProperties();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
            System.out.println("Миграция выполнена");
        } catch (SQLException | IOException | DatabaseException e) {
            throw new RuntimeException(e);
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}