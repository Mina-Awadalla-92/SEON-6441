package com.Game.observer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameLoggerTest {

    @Mock
    private LogEntryBuffer mockLogEntryBuffer;

    @Mock
    private FileLogObserver mockFileLogObserver;

    private GameLogger gameLogger;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Reset the singleton instance before each test
        resetSingleton();

        // Get the singleton instance of GameLogger
        gameLogger = GameLogger.getInstance("log.txt", true);

        // Use reflection to inject mocked dependencies into the private fields
        injectMockedDependencies(gameLogger);
    }

    @After
    public void tearDown() {
        // Reset the singleton instance after each test to ensure isolation
        resetSingleton();
    }

    // Helper method to inject mocked dependencies into private fields
    private void injectMockedDependencies(GameLogger gameLogger) throws Exception {
        // Inject the mocked LogEntryBuffer
        Field logEntryBufferField = GameLogger.class.getDeclaredField("d_logEntryBuffer");
        logEntryBufferField.setAccessible(true);
        logEntryBufferField.set(gameLogger, mockLogEntryBuffer);

        // Inject the mocked FileLogObserver
        Field fileLogObserverField = GameLogger.class.getDeclaredField("d_fileObserver");
        fileLogObserverField.setAccessible(true);
        fileLogObserverField.set(gameLogger, mockFileLogObserver);
    }

    // Helper method to reset the GameLogger singleton instance
    private void resetSingleton() {
        // Use reflection to access and reset the static singleton instance field
        try {
            Field instanceField = GameLogger.class.getDeclaredField("d_instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null); // Reset the singleton instance to null
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogAction() {
        String logEntry = "Player1 attacked Player2";

        // When logAction is called, it should forward the action to the LogEntryBuffer
        gameLogger.logAction(logEntry);

        verify(mockLogEntryBuffer).logAction(logEntry);
    }

    @Test
    public void testLogPhaseChange() {
        String phaseName = "Combat";

        // When logPhaseChange is called, it should forward the phase change to the LogEntryBuffer
        gameLogger.logPhaseChange(phaseName);

        verify(mockLogEntryBuffer).logPhaseChange(phaseName);
    }

    @Test
    public void testGetLogBuffer() {
        // When getLogBuffer is called, it should return the log buffer content from LogEntryBuffer
        when(mockLogEntryBuffer.getLogBuffer()).thenReturn("Mock log buffer content");

        String logBuffer = gameLogger.getLogBuffer();

        assertEquals("Mock log buffer content", logBuffer, "The log buffer content should match the mock content.");
    }

    @Test
    public void testClearLogBuffer() {
        // When clearLogBuffer is called, it should invoke clearLogBuffer on the LogEntryBuffer
        gameLogger.clearLogBuffer();

        verify(mockLogEntryBuffer).clearLogBuffer();
    }

    @Test
    public void testGetLogFilePath() {
        String logFilePath = "log.txt";
        when(mockFileLogObserver.getLogFilePath()).thenReturn(logFilePath);

        assertEquals(logFilePath, gameLogger.getLogFilePath(), "The log file path should match the expected path.");
    }

    @Test
    public void testSetLogFilePath() {
        String newLogFilePath = "new_log.txt";

        // When setLogFilePath is called, it should update the file path in FileLogObserver
        gameLogger.setLogFilePath(newLogFilePath);

        verify(mockFileLogObserver).setLogFilePath(newLogFilePath);
    }
}
