package org.ylab.homework.utiltest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ylab.homework.homework_1.util.UserAuditLogger;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;


@DisplayName("Testing UserAuditLogger class")
public class UserAuditLoggerTest {

    private List<String> mockAuditLog;

    @BeforeEach
    public void setUp() {
        mockAuditLog = mock(List.class);
        UserAuditLogger.auditLog = mockAuditLog;
    }

    @Test
    @DisplayName("Test logging user registration")
    public void testLogUserRegistration() {
        String username = "testUser";
        UserAuditLogger.logUserRegistration(username);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    @DisplayName("Test logging user login")
    public void testLogUserLoggedIn() {
        String username = "testUser";
        UserAuditLogger.logUserLoggedIn(username);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    @DisplayName("Test logging training addition")
    public void testLogTrainingAdded() {
        String username = "testUser";
        LocalDate date = LocalDate.now();
        String type = "Running";
        UserAuditLogger.logTrainingAdded(username, date, type);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    @DisplayName("Test logging training deletion")
    public void testLogTrainingDeleted() {
        String username = "testUser";
        LocalDate date = LocalDate.now();
        String type = "Running";
        UserAuditLogger.logTrainingDeleted(username, date, type);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    @DisplayName("Test logging training edition")
    public void testLogTrainingEdited() {
        String username = "testUser";
        LocalDate date = LocalDate.now();
        String type = "Running";
        UserAuditLogger.logTrainingEdited(username, date, type);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    @DisplayName("Test logging failed login attempt")
    public void testLogFailedLoginAttempt() {
        String username = "testUser";
        UserAuditLogger.logFailedLoginAttempt(username);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    @DisplayName("Test logging user logout")
    public void testLogLogout() {
        String username = "testUser";
        UserAuditLogger.logLogout(username);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    @DisplayName("Test printing audit log")
    public void testPrintAuditLog() {
        List<String> auditLog = List.of(
                "Пользователь testUser зарегистрировался 2024-04-11T12:00:00",
                "Пользователь testUser произвел вход в приложение в 2024-04-11T12:00:00"
        );
        UserAuditLogger.auditLog = auditLog;
        UserAuditLogger.printAuditLog();
    }
}
