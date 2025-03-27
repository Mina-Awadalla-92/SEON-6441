package com.Game.observer;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Test class for the Observer pattern implementation.
 * Tests the logging functionality using LogEntryBuffer, FileLogObserver, and GameLogger.
 */
public class ObserverPatternTest {
    
    private LogEntryBuffer d_logBuffer;
    private FileLogObserver d_fileObserver;
    private GameLogger d_gameLogger;
    private String d_testLogFilePath = "test_log.log";
    
    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void setUp() {
        // Create a test log file
        d_fileObserver = new FileLogObserver(d_testLogFilePath, false);
        d_logBuffer = new LogEntryBuffer();
        d_logBuffer.addObserver(d_fileObserver);
        
        // Create a new GameLogger instance with the test file
        d_gameLogger = GameLogger.getInstance(d_testLogFilePath, false);
    }
    
    /**
     * Cleans up after tests.
     */
    @After
    public void tearDown() {
        // Delete the test log file
        File testLogFile = new File(d_testLogFilePath);
        if (testLogFile.exists()) {
            testLogFile.delete();
        }
    }
    
    /**
     * Tests that the LogEntryBuffer correctly notifies its observers.
     */
    @Test
    public void testLogBufferNotifiesObservers() {
        // Create a test log entry
        String testLogEntry = "Test log entry";
        
        // Log the entry
        d_logBuffer.logAction(testLogEntry);
        
        // Check that the log buffer contains the entry
        assertTrue("Log buffer should contain the log entry", 
                d_logBuffer.getLogBuffer().contains(testLogEntry));
        
        // Now check that the file contains the log entry
        try {
            Thread.sleep(100); // Give a moment for file writing to complete
            
            BufferedReader reader = new BufferedReader(new FileReader(d_testLogFilePath));
            String line = null;
            boolean found = false;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains(testLogEntry)) {
                    found = true;
                    break;
                }
            }
            
            reader.close();
            assertTrue("Log file should contain the log entry", found);
            
        } catch (IOException | InterruptedException e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }
    
    /**
     * Tests that the GameLogger correctly logs actions.
     */
    @Test
    public void testGameLoggerLogsActions() {
        // Create a test log message
        String testMessage = "Test game action";
        
        // Log the action
        d_gameLogger.logAction(testMessage);
        
        // Check that the log file contains the message
        try {
            Thread.sleep(100); // Give a moment for file writing to complete
            
            BufferedReader reader = new BufferedReader(new FileReader(d_testLogFilePath));
            String line = null;
            boolean found = false;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains(testMessage)) {
                    found = true;
                    break;
                }
            }
            
            reader.close();
            assertTrue("Log file should contain the action message", found);
            
        } catch (IOException | InterruptedException e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }
    
    /**
     * Tests that the GameLogger correctly logs phase changes.
     */
    @Test
    public void testGameLoggerLogsPhaseChanges() {
        // Create a test phase name
        String testPhaseName = "TEST_PHASE";
        
        // Log the phase change
        d_gameLogger.logPhaseChange(testPhaseName);
        
        // Check that the log file contains the phase change
        try {
            Thread.sleep(100); // Give a moment for file writing to complete
            
            BufferedReader reader = new BufferedReader(new FileReader(d_testLogFilePath));
            String line = null;
            boolean found = false;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains(testPhaseName) && line.contains("PHASE")) {
                    found = true;
                    break;
                }
            }
            
            reader.close();
            assertTrue("Log file should contain the phase change", found);
            
        } catch (IOException | InterruptedException e) {
            fail("Exception occurred while reading log file: " + e.getMessage());
        }
    }
    
    /**
     * Tests adding and removing observers from the LogEntryBuffer.
     */
    @Test
    public void testAddAndRemoveObservers() {
        // Create a second test log file
        String secondLogFilePath = "second_test_log.log";
        FileLogObserver secondObserver = new FileLogObserver(secondLogFilePath, false);
        
        // Add the second observer
        d_logBuffer.addObserver(secondObserver);
        
        // Log an entry
        String testLogEntry = "Test for multiple observers";
        d_logBuffer.logAction(testLogEntry);
        
        // Check both files contain the entry
        try {
            Thread.sleep(100); // Give a moment for file writing to complete
            
            // Check first file
            BufferedReader reader1 = new BufferedReader(new FileReader(d_testLogFilePath));
            boolean found1 = checkFileForEntry(reader1, testLogEntry);
            reader1.close();
            
            // Check second file
            BufferedReader reader2 = new BufferedReader(new FileReader(secondLogFilePath));
            boolean found2 = checkFileForEntry(reader2, testLogEntry);
            reader2.close();
            
            assertTrue("First log file should contain the entry", found1);
            assertTrue("Second log file should contain the entry", found2);
            
            // Now remove the second observer
            d_logBuffer.removeObserver(secondObserver);
            
            // Log another entry
            String secondEntry = "Test after removal";
            d_logBuffer.logAction(secondEntry);
            
            Thread.sleep(100); // Give a moment for file writing to complete
            
            // Check first file again
            reader1 = new BufferedReader(new FileReader(d_testLogFilePath));
            boolean foundSecondEntry1 = checkFileForEntry(reader1, secondEntry);
            reader1.close();
            
            // Check second file again
            reader2 = new BufferedReader(new FileReader(secondLogFilePath));
            boolean foundSecondEntry2 = checkFileForEntry(reader2, secondEntry);
            reader2.close();
            
            assertTrue("First log file should contain the second entry", foundSecondEntry1);
            assertFalse("Second log file should NOT contain the second entry", foundSecondEntry2);
            
            // Clean up second log file
            File secondLogFile = new File(secondLogFilePath);
            if (secondLogFile.exists()) {
                secondLogFile.delete();
            }
            
        } catch (IOException | InterruptedException e) {
            fail("Exception occurred while testing multiple observers: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to check if a file contains a specific entry.
     */
    private boolean checkFileForEntry(BufferedReader reader, String entry) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(entry)) {
                return true;
            }
        }
        return false;
    }
}