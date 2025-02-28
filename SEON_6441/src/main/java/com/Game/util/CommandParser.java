package com.Game.util;

/**
 * Utility for parsing and validating commands.
 */
public class CommandParser {
    /**
     * Parses a command string into command parts.
     * 
     * @param p_commandString The command string to parse.
     * @return An array of command parts.
     */
    public static String[] parseCommand(String p_commandString) {
        return p_commandString.trim().split("\\s+");
    }
    
    /**
     * Validates that a command has the required number of arguments.
     * 
     * @param p_commandParts The command parts.
     * @param p_minArgs The minimum number of arguments required.
     * @return True if the command has enough arguments, false otherwise.
     */
    public static boolean hasRequiredArgs(String[] p_commandParts, int p_minArgs) {
        return p_commandParts.length >= p_minArgs;
    }
    
}