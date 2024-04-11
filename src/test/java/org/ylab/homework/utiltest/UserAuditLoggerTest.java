package org.ylab.homework.utiltest;
import org.ylab.homework.homework_1.util.UserAuditLogger;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;




public class UserAuditLoggerTest {

    private List<String> mockAuditLog;

    @BeforeEach
    public void setUp() {
        mockAuditLog = mock(List.class);
        UserAuditLogger.auditLog = mockAuditLog;
    }

    @Test
    public void testLogUserRegistration() {
        String username = "testUser";
        UserAuditLogger.logUserRegistration(username);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    public void testLogUserLoggedIn() {
        String username = "testUser";
        UserAuditLogger.logUserLoggedIn(username);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    public void testLogTrainingAdded() {
        String username = "testUser";
        LocalDate date = LocalDate.now();
        String type = "Running";
        UserAuditLogger.logTrainingAdded(username, date, type);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    public void testLogTrainingDeleted() {
        String username = "testUser";
        LocalDate date = LocalDate.now();
        String type = "Running";
        UserAuditLogger.logTrainingDeleted(username, date, type);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    public void testLogTrainingEdited() {
        String username = "testUser";
        LocalDate date = LocalDate.now();
        String type = "Running";
        UserAuditLogger.logTrainingEdited(username, date, type);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    public void testLogFailedLoginAttempt() {
        String username = "testUser";
        UserAuditLogger.logFailedLoginAttempt(username);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    public void testLogLogout() {
        String username = "testUser";
        UserAuditLogger.logLogout(username);
        verify(mockAuditLog).add(anyString());
    }

    @Test
    public void testPrintAuditLog() {
        List<String> auditLog = List.of(
                "Пользователь testUser зарегистрировался 2024-04-11T12:00:00",
                "Пользователь testUser произвел вход в приложение в 2024-04-11T12:00:00"
        );
        UserAuditLogger.auditLog = auditLog;
        UserAuditLogger.printAuditLog();
    }
}
