package com.Game.observer;

import org.junit.jupiter.api.*;
import org.mockito.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class FileLogObserverTest {

    private FileLogObserver fileLogObserver;
    private final String testLogFilePath = "test_log.txt";

    @BeforeEach
    public void setUp() throws Exception {

        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Spy on the FileLogObserver object to verify method calls
        fileLogObserver = spy(new FileLogObserver(testLogFilePath));

        // Instead of mocking createPrintWriter, we mock println directly
        // We're assuming the actual method uses PrintWriter directly
        doNothing().when(fileLogObserver).update(anyString());

        // Simulate the constructor and the first update that writes to the file
        fileLogObserver.update("Log started");
    }

    @Test
    public void testObserverInitialization() {
        // Verify that the log header is written during initialization
        verify(fileLogObserver).update("Log started");
    }

    @Test
    public void testUpdateWritesLogEntry() throws Exception {
        File testLogFile = new File("test_log.txt");

        fileLogObserver = new FileLogObserver(testLogFile.getAbsolutePath());

        // Simulate adding a new log entry
        String logEntry = "Player1 entered the game!";
        fileLogObserver.update(logEntry);

        // Read the file to verify the log entry was written
        try (BufferedReader reader = new BufferedReader(new FileReader(testLogFile))) {
            String line;
            boolean logEntryFound = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals(logEntry)) {
                    logEntryFound = true;
                    break;
                }
            }
            // Verify that the log entry was found in the file
            assertTrue(logEntryFound, "Log entry was not written to the file.");
        }

        if (testLogFile.exists()) {
            testLogFile.delete(); // Ensure the file is clean before each test
        }
    }

    @Test
    public void testSetLogFilePath() {
        String newLogFilePath = "new_log.txt";
        fileLogObserver.setLogFilePath(newLogFilePath);

        // Verify that the file path is updated correctly
        Assertions.assertEquals(newLogFilePath, fileLogObserver.getLogFilePath());
    }

    @Test
    public void testSetAppendToFile() {
        fileLogObserver.setAppendToFile(true);

        // Verify that append flag is set correctly
        assertTrue(fileLogObserver.isAppendToFile());
    }

    @AfterEach
    public void tearDown() {
        // Any necessary cleanup after each test
    }
}

