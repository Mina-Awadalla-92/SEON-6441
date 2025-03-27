package com.Game.observer;

/**
 * Observer interface for the Observer pattern implementation.
 * Classes implementing this interface will observe and react to changes in a LogEntryBuffer.
 */
public interface LogObserver {
    
    /**
     * Update method called when the observed LogEntryBuffer changes.
     * 
     * @param p_logEntry The log entry that was added to the buffer
     */
    void update(String p_logEntry);
}