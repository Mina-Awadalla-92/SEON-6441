package com.Game.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for command validation.
 * Contains methods to validate various commands including tournament commands.
 */
public class CommandValidator {
    
    /**
     * Validates a tournament command.
     * 
     * @param p_command Full tournament command string
     * @return true if valid, false otherwise
     */
    public static boolean validateTournamentCommand(String p_command) {
        String[] parts = p_command.split("\\s+");
        
        // Basic syntax check
        if (parts.length < 9 || !parts[0].equalsIgnoreCase("tournament")) {
            return false;
        }
        
        boolean hasM = false;
        boolean hasP = false;
        boolean hasG = false;
        boolean hasD = false;
        
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].equals("-M")) hasM = true;
            if (parts[i].equals("-P")) hasP = true;
            if (parts[i].equals("-G")) hasG = true;
            if (parts[i].equals("-D")) hasD = true;
        }
        
        return hasM && hasP && hasG && hasD;
    }
    
    /**
     * Parses a tournament command and validates parameter values.
     * 
     * @param p_commandParts Tournament command split into parts
     * @return true if all parameter values are valid, false otherwise
     */
    public static boolean validateTournamentParameters(String[] p_commandParts) {
        List<String> mapFiles = extractParameterValues(p_commandParts, "-M");
        List<String> playerStrategies = extractParameterValues(p_commandParts, "-G");
        List<String> gameNumbers = extractParameterValues(p_commandParts, "-G");
        List<String> maxTurns = extractParameterValues(p_commandParts, "-D");
        
        // Check for missing parameters
        if (mapFiles.isEmpty() || playerStrategies.isEmpty() || 
            gameNumbers.isEmpty() || maxTurns.isEmpty()) {
            return false;
        }
        
        // Validate map count (1-5)
        if (mapFiles.size() < 1 || mapFiles.size() > 5) {
            return false;
        }
        
        // Validate player strategies (2-4)
        if (playerStrategies.size() < 2 || playerStrategies.size() > 4) {
            return false;
        }
        
        // Validate strategies are valid
        for (String strategy : playerStrategies) {
            if (!TournamentUtil.isValidStrategy(strategy)) {
                return false;
            }
        }
        
        // Validate games number (1-5)
        try {
            int games = Integer.parseInt(gameNumbers.get(0));
            if (games < 1 || games > 5) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        
        // Validate max turns (10-50)
        try {
            int turns = Integer.parseInt(maxTurns.get(0));
            if (turns < 10 || turns > 50) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Extracts values for a parameter from a command.
     * 
     * @param p_commandParts Command split into parts
     * @param p_paramName Parameter name to extract values for
     * @return List of parameter values
     */
    private static List<String> extractParameterValues(String[] p_commandParts, String p_paramName) {
        List<String> values = new java.util.ArrayList<>();
        
        for (int i = 0; i < p_commandParts.length; i++) {
            if (p_commandParts[i].equals(p_paramName)) {
                i++; // Move to the first value
                
                // Collect values until the next parameter or end of command
                while (i < p_commandParts.length && !p_commandParts[i].startsWith("-")) {
                    values.add(p_commandParts[i]);
                    i++;
                }
                
                break;
            }
        }
        
        return values;
    }
}