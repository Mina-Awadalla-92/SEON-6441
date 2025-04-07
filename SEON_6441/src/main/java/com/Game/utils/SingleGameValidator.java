package com.Game.utils;

import java.util.List;
import java.util.Arrays;

/**
 * Utility class for validating single game mode configuration.
 */
public class SingleGameValidator {
    
    /**
     * Validates the map file for single game mode.
     * 
     * @param mapFile The path to the map file
     * @return true if the map is valid, false otherwise
     */
    public static boolean validateMapFile(String mapFile) {
        if (mapFile == null || mapFile.trim().isEmpty()) {
            return false;
        }
        
        // Check file extension
        if (!mapFile.toLowerCase().endsWith(".map")) {
            return false;
        }
        
        // Use MapLoader to perform comprehensive map validation
        MapLoader mapLoader = new MapLoader();
        return mapLoader.isMapCompletelyValid(mapFile);
    }
    
    /**
     * Validates the player strategies for single game mode.
     * 
     * @param playerStrategies List of player strategies
     * @return true if the strategies are valid, false otherwise
     */
    public static boolean validatePlayerStrategies(List<String> playerStrategies) {
        // Validate number of players
        if (playerStrategies == null || playerStrategies.size() < 2 || playerStrategies.size() > 5) {
            return false;
        }
        
        // List of valid strategies
        List<String> validStrategies = Arrays.asList(
            "human", "aggressive", "benevolent", "random", "cheater"
        );
        
        // Check each strategy
        for (String strategy : playerStrategies) {
            if (strategy == null || !validStrategies.contains(strategy.toLowerCase())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Validates the maximum number of turns for single game mode.
     * 
     * @param maxTurns The maximum number of turns
     * @return true if the turn count is valid, false otherwise
     */
    public static boolean validateMaxTurns(int maxTurns) {
        return maxTurns >= 10 && maxTurns <= 50;
    }
    
    /**
     * Comprehensive validation for single game mode settings.
     * 
     * @param mapFile The path to the map file
     * @param playerStrategies List of player strategies
     * @param maxTurns Maximum number of turns
     * @return true if all settings are valid, false otherwise
     */
    public static boolean validateSingleGameSettings(
            String mapFile, 
            List<String> playerStrategies, 
            int maxTurns) {
        return validateMapFile(mapFile) &&
               validatePlayerStrategies(playerStrategies) &&
               validateMaxTurns(maxTurns);
    }
    
    /**
     * Provides a detailed error message for invalid game settings.
     * 
     * @param mapFile The path to the map file
     * @param playerStrategies List of player strategies
     * @param maxTurns Maximum number of turns
     * @return A string describing the validation errors
     */
    public static String getValidationErrorMessage(
            String mapFile, 
            List<String> playerStrategies, 
            int maxTurns) {
        StringBuilder errorMessage = new StringBuilder("Invalid game settings:\n");
        
        // Validate map file
        if (!validateMapFile(mapFile)) {
            errorMessage.append("- Invalid map file. Ensure the file exists and is a valid .map file.\n");
        }
        
        // Validate player strategies
        if (!validatePlayerStrategies(playerStrategies)) {
            errorMessage.append("- Invalid player strategies. Must have 2-5 valid strategies ")
                        .append("(human, aggressive, benevolent, random, cheater).\n");
        }
        
        // Validate max turns
        if (!validateMaxTurns(maxTurns)) {
            errorMessage.append("- Invalid number of turns. Must be between 10 and 50.\n");
        }
        
        return errorMessage.toString();
    }
}