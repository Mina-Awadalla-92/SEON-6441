package com.Game.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * LogEntryBuffer class acts as the Observable in the Observer pattern.
 * It maintains a list of log entries and notifies observers when new entries are added.
 */
public class LogEntryBuffer {
    
    /**
     * List of observers that will be notified of changes.
     */
    private List<LogObserver> d_observers;
    
    /**
     * The current log entry buffer.
     */
    private StringBuilder d_logBuffer;
    
    /**
     * Constructor initializing the buffer and observer list.
     */
    public LogEntryBuffer() {
        this.d_observers = new ArrayList<>();
        this.d_logBuffer = new StringBuilder();
    }
    
    /**
     * Adds an observer to be notified of log entries.
     * 
     * @param p_observer The observer to add
     */
    public void addObserver(LogObserver p_observer) {
        if (!d_observers.contains(p_observer)) {
            d_observers.add(p_observer);
        }
    }
    
    /**
     * Removes an observer.
     * 
     * @param p_observer The observer to remove
     */
    public void removeObserver(LogObserver p_observer) {
        d_observers.remove(p_observer);
    }
    
    /**
     * Notifies all observers about a new log entry.
     * 
     * @param p_logEntry The log entry to notify observers about
     */
    private void notifyObservers(String p_logEntry) {
        for (LogObserver l_observer : d_observers) {
            l_observer.update(p_logEntry);
        }
    }
    
    /**
     * Adds a log entry to the buffer and notifies observers.
     * 
     * @param p_logEntry The log entry to add
     */
    public void logAction(String p_logEntry) {
        String l_formattedEntry = formatLogEntry(p_logEntry);
        d_logBuffer.append(l_formattedEntry).append("\n");
        notifyObservers(l_formattedEntry);
    }
    
    /**
     * Logs a phase change in the game.
     * 
     * @param p_phaseName The name of the new phase
     */
    public void logPhaseChange(String p_phaseName) {
        String l_phaseEntry = "=== " + p_phaseName.toUpperCase() + " PHASE ===";
        logAction(l_phaseEntry);
    }
    
    /**
     * Formats a log entry with timestamp.
     * 
     * @param p_logEntry The log entry to format
     * @return The formatted log entry
     */
    private String formatLogEntry(String p_logEntry) {
        java.time.LocalDateTime l_now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter l_formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "[" + l_formatter.format(l_now) + "] " + p_logEntry;
    }
    
    /**
     * Gets the current log buffer content.
     * 
     * @return The log buffer as a string
     */
    public String getLogBuffer() {
        return d_logBuffer.toString();
    }
    
    /**
     * Clears the log buffer.
     */
    public void clearLogBuffer() {
        d_logBuffer = new StringBuilder();
    }
}