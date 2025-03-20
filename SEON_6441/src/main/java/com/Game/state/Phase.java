package com.Game.state;

/**
 * Interface representing a game phase in the State pattern.
 * Different phases of the game will implement this interface to provide phase-specific behavior.
 * This is part of the foundation for the State pattern, which will be fully implemented in Build 2.
 */
public interface Phase {
    
    /**
     * Called when entering this phase. Performs any initialization needed for the phase.
     */
    void enter();
    
    /**
     * Handles commands specific to this phase.
     * 
     * @param p_commandParts The parts of the command (split by spaces)
     */
    void handleCommand(String[] p_commandParts);
    
    /**
     * Called when exiting this phase. Performs any cleanup needed.
     */
    void exit();
    
    /**
     * Gets the type/name of this phase.
     * 
     * @return A string identifying the phase
     */
    String getPhaseType();
}