package com.Game.observer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * FileLogObserver class implements the LogObserver interface.
 * It writes log entries to a file when notified of changes in the LogEntryBuffer.
 */
public class FileLogObserver implements LogObserver {
    
    /**
     * The path of the log file.
     */
    private String d_logFilePath;
    
    /**
     * Flag indicating whether to append to an existing file or create a new one.
     */
    private boolean d_appendToFile;
    
    /**
     * Constructor initializing the observer with a log file path.
     * By default, it will create a new log file, replacing any existing one.
     * 
     * @param p_logFilePath The path of the log file
     */
    public FileLogObserver(String p_logFilePath) {
        this(p_logFilePath, false);
    }
    
    /**
     * Constructor initializing the observer with a log file path and append flag.
     * 
     * @param p_logFilePath The path of the log file
     * @param p_appendToFile If true, append to existing file; if false, create new file
     */
    public FileLogObserver(String p_logFilePath, boolean p_appendToFile) {
        this.d_logFilePath = p_logFilePath;
        this.d_appendToFile = p_appendToFile;
        
        // Initialize the log file
        if (!p_appendToFile) {
            try (PrintWriter l_writer = new PrintWriter(d_logFilePath)) {
                l_writer.println("=== WARZONE GAME LOG ===");
                l_writer.println("Log started at: " + java.time.LocalDateTime.now());
                l_writer.println("==============================");
            } catch (IOException e) {
                System.err.println("Error initializing log file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Update method called when observed LogEntryBuffer changes.
     * Writes the new log entry to the file.
     * 
     * @param p_logEntry The log entry that was added to the buffer
     */
    @Override
    public void update(String p_logEntry) {
        try (FileWriter l_fw = new FileWriter(d_logFilePath, true);
             BufferedWriter l_bw = new BufferedWriter(l_fw);
             PrintWriter l_out = new PrintWriter(l_bw)) {
            
            l_out.println(p_logEntry);
            
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
    
    /**
     * Gets the log file path.
     * 
     * @return The log file path
     */
    public String getLogFilePath() {
        return d_logFilePath;
    }
    
    /**
     * Sets the log file path.
     * 
     * @param p_logFilePath The new log file path
     */
    public void setLogFilePath(String p_logFilePath) {
        this.d_logFilePath = p_logFilePath;
    }
    
    /**
     * Checks if appending to existing file.
     * 
     * @return true if appending, false if creating new file
     */
    public boolean isAppendToFile() {
        return d_appendToFile;
    }
    
    /**
     * Sets whether to append to existing file.
     * 
     * @param p_appendToFile If true, append to existing file; if false, create new file
     */
    public void setAppendToFile(boolean p_appendToFile) {
        this.d_appendToFile = p_appendToFile;
    }
}