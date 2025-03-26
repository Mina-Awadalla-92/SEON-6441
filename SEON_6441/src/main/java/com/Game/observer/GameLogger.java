package com.Game.observer;

/**
 * GameLogger class serves as a facade for the logging system.
 * It initializes and manages the LogEntryBuffer and observers, providing a simplified
 * interface for the game components to log actions.
 */
public class GameLogger {
    
    /**
     * The log entry buffer that holds and distributes log entries.
     */
    private LogEntryBuffer d_logEntryBuffer;
    
    /**
     * File observer that writes logs to a file.
     */
    private FileLogObserver d_fileObserver;
    
    /**
     * Singleton instance of the GameLogger.
     */
    private static GameLogger d_instance;
    
    /**
     * Private constructor initializing the logger components.
     * 
     * @param p_logFilePath The path of the log file
     * @param p_appendToFile If true, append to existing file; if false, create new file
     */
    private GameLogger(String p_logFilePath, boolean p_appendToFile) {
        this.d_logEntryBuffer = new LogEntryBuffer();
        this.d_fileObserver = new FileLogObserver(p_logFilePath, p_appendToFile);
        
        // Register the file observer with the buffer
        this.d_logEntryBuffer.addObserver(d_fileObserver);
    }
    
    /**
     * Gets the singleton instance of GameLogger.
     * Creates a new instance if one doesn't exist.
     * 
     * @param p_logFilePath The path of the log file
     * @param p_appendToFile If true, append to existing file; if false, create new file
     * @return The GameLogger singleton instance
     */
    public static GameLogger getInstance(String p_logFilePath, boolean p_appendToFile) {
        if (d_instance == null) {
            d_instance = new GameLogger(p_logFilePath, p_appendToFile);
        }
        return d_instance;
    }
    
    /**
     * Gets the singleton instance with default configuration if it exists.
     * 
     * @return The existing GameLogger instance or null if not initialized
     */
    public static GameLogger getInstance() {
        return d_instance;
    }
    
    /**
     * Logs a game action.
     * 
     * @param p_logEntry The log entry describing the action
     */
    public void logAction(String p_logEntry) {
        this.d_logEntryBuffer.logAction(p_logEntry);
    }
    
    /**
     * Logs a phase change in the game.
     * 
     * @param p_phaseName The name of the new phase
     */
    public void logPhaseChange(String p_phaseName) {
        this.d_logEntryBuffer.logPhaseChange(p_phaseName);
    }
    
    /**
     * Gets the full log buffer content.
     * 
     * @return The log buffer as a string
     */
    public String getLogBuffer() {
        return this.d_logEntryBuffer.getLogBuffer();
    }
    
    /**
     * Clears the log buffer.
     */
    public void clearLogBuffer() {
        this.d_logEntryBuffer.clearLogBuffer();
    }
    
    /**
     * Gets the file path of the log file.
     * 
     * @return The log file path
     */
    public String getLogFilePath() {
        return this.d_fileObserver.getLogFilePath();
    }
    
    /**
     * Sets the log file path.
     * 
     * @param p_logFilePath The new log file path
     */
    public void setLogFilePath(String p_logFilePath) {
        this.d_fileObserver.setLogFilePath(p_logFilePath);
    }
}