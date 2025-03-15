package com.Game.command;

/**
 * Interface representing a command in the Command pattern.
 * All game commands will implement this interface to provide command-specific behavior.
 * This is part of the foundation for the Command pattern, which will be fully implemented in Build 2.
 */
public interface Command {
    
    /**
     * Executes the command, performing the associated action.
     */
    void execute();
    
    /**
     * Undoes the command, reversing its effects if possible.
     * This method is provided for potential future undo functionality.
     */
    void undo();
    
    /**
     * Gets the name or type of the command.
     * 
     * @return A string identifying the command
     */
    String getCommandName();
    
    /**
     * Validates whether the command can be executed in the current game state.
     * 
     * @return true if the command can be executed, false otherwise
     */
    boolean validate();
}