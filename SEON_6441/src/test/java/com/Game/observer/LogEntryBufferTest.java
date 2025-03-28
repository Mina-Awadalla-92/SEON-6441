package com.Game.observer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class LogEntryBufferTest {

    @Mock
    private LogObserver mockLogObserver;

    private LogEntryBuffer logEntryBuffer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the LogEntryBuffer instance
        logEntryBuffer = new LogEntryBuffer();
    }

    @Test
    void testAddObserver() {
        // Add the observer
        logEntryBuffer.addObserver(mockLogObserver);

        // Verify that the observer is added
        // In this case, we can't directly check the observers list, but we can verify interactions
        logEntryBuffer.logAction("Test action");

        // Verify that update was called on the observer when logAction is called
        verify(mockLogObserver).update(any(String.class));
    }

    @Test
    void testRemoveObserver() {
        // Add the observer and then remove it
        logEntryBuffer.addObserver(mockLogObserver);
        logEntryBuffer.removeObserver(mockLogObserver);

        // Log an action and verify that the observer is not notified
        logEntryBuffer.logAction("Test action");

        // Verify that the observer's update method is not called since it was removed
        verify(mockLogObserver, never()).update(any(String.class));
    }

    @Test
    void testLogAction() {
        // Add the observer to ensure that it will be notified
        logEntryBuffer.addObserver(mockLogObserver);

        String logEntry = "Player1 attacked Player2";

        // Log the action
        logEntryBuffer.logAction(logEntry);

        // Verify that the log entry is formatted and appended to the buffer
        String expectedLogBuffer = logEntryBuffer.getLogBuffer();
        assertTrue(expectedLogBuffer.contains(logEntry), "The log entry should be present in the buffer.");

        // Verify that update was called on the observer with the formatted log entry
        verify(mockLogObserver).update(any(String.class));
    }

    @Test
    void testLogPhaseChange() {
        // Add the observer to ensure that it will be notified
        logEntryBuffer.addObserver(mockLogObserver);

        String phaseName = "MAP EDITING";

        // Log the phase change
        logEntryBuffer.logPhaseChange(phaseName);

        // Verify that the phase change message is correctly formatted
        String expectedLogBuffer = logEntryBuffer.getLogBuffer();
        assertTrue(expectedLogBuffer.contains("=== MAP EDITING PHASE ==="), "The phase change message should be present in the buffer.");

        // Verify that update was called on the observer with the formatted phase change entry
        verify(mockLogObserver).update(any(String.class));
    }

    @Test
    void testGetLogBuffer() {
        String logEntry = "Player1 attacked Player2";

        // Log an action
        logEntryBuffer.logAction(logEntry);

        // Verify that the log buffer contains the log entry
        assertTrue(logEntryBuffer.getLogBuffer().contains(logEntry), "The log buffer should contain the logged entry.");
    }

    @Test
    void testClearLogBuffer() {
        // Add an observer
        logEntryBuffer.addObserver(mockLogObserver);

        // Log an action to add content to the buffer
        logEntryBuffer.logAction("Test action");

        // Verify that the log buffer is not empty
        assertFalse(logEntryBuffer.getLogBuffer().isEmpty(), "The log buffer should not be empty.");

        // Clear the buffer
        logEntryBuffer.clearLogBuffer();

        // Verify that the log buffer is now empty
        assertTrue(logEntryBuffer.getLogBuffer().isEmpty(), "The log buffer should be empty after clearing.");

        // Verify that the observer was still called before the buffer was cleared
        verify(mockLogObserver).update(any(String.class));
    }

    @Test
    void testFormatLogEntry() throws Exception {
        // Access the private method 'formatLogEntry' via reflection
        java.lang.reflect.Method formatMethod = LogEntryBuffer.class.getDeclaredMethod("formatLogEntry", String.class);
        formatMethod.setAccessible(true);

        String logEntry = "Player1 attacked Player2";

        // Call the private method to format the log entry
        String formattedEntry = (String) formatMethod.invoke(logEntryBuffer, logEntry);

        // Check that the formatted entry contains the timestamp and the original log entry
        assertTrue(formattedEntry.contains(logEntry), "The formatted log entry should contain the original log entry.");
        assertTrue(formattedEntry.contains("["), "The formatted log entry should contain a timestamp.");
    }
}

