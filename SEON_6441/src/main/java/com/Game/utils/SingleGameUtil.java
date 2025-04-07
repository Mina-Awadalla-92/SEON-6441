package com.Game.utils;

import com.Game.model.Player;
import com.Game.model.AggressivePlayer;
import com.Game.model.BenevolentPlayer;
import com.Game.model.RandomPlayer;
import com.Game.model.CheaterPlayer;
import com.Game.model.HumanPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for Single Game Mode operations.
 * Provides helper methods for player creation, strategy management, and game analysis.
 */
public class SingleGameUtil {
    
    /**
     * Creates a player based on the specified strategy.
     * 
     * @param strategy The player strategy (human, aggressive, benevolent, random, cheater)
     * @param playerName The name to assign to the player
     * @return A Player object with the specified strategy
     * @throws IllegalArgumentException if an invalid strategy is provided
     */
    public static Player createPlayerByStrategy(String strategy, String playerName) {
        switch (strategy.toLowerCase()) {
            case "human":
                return new HumanPlayer(playerName, "human");
            case "aggressive":
                return new AggressivePlayer(playerName, "aggressive");
            case "benevolent":
                return new BenevolentPlayer(playerName, "benevolent");
            case "random":
                return new RandomPlayer(playerName, "random");
            case "cheater":
                return new CheaterPlayer(playerName, "cheater");
            default:
                throw new IllegalArgumentException("Invalid player strategy: " + strategy);
        }
    }
    
    /**
     * Generates unique player names based on their strategy.
     * 
     * @param strategies List of player strategies
     * @return List of generated player names
     */
    public static List<String> generatePlayerNames(List<String> strategies) {
        return strategies.stream()
            .map(strategy -> generateUniquePlayerName(strategy))
            .collect(Collectors.toList());
    }
    
    /**
     * Generates a unique player name for a given strategy.
     * 
     * @param strategy The player strategy
     * @return A unique player name
     */
    private static String generateUniquePlayerName(String strategy) {
        return strategy.substring(0, 1).toUpperCase() + 
               strategy.substring(1) + "_" + 
               System.currentTimeMillis() % 1000;
    }
    
    /**
     * Analyzes the player strategies in a single game.
     * 
     * @param strategies List of player strategies
     * @return A map containing strategy analysis
     */
    public static Map<String, Object> analyzeGameStrategies(List<String> strategies) {
        Map<String, Object> analysis = new HashMap<>();
        
        // Count strategy types
        Map<String, Long> strategyCounts = strategies.stream()
            .collect(Collectors.groupingBy(
                String::toLowerCase, 
                Collectors.counting()
            ));
        
        // Determine if game is fully automatic
        boolean isFullyAutomatic = strategies.stream()
            .noneMatch(strategy -> strategy.equalsIgnoreCase("human"));
        
        analysis.put("strategyCounts", strategyCounts);
        analysis.put("isFullyAutomatic", isFullyAutomatic);
        analysis.put("totalPlayers", strategies.size());
        
        return analysis;
    }
    
    /**
     * Provides strategy recommendations based on current strategy composition.
     * 
     * @param strategies Current list of strategies
     * @return List of recommended strategies to balance the game
     */
    public static List<String> getStrategyRecommendations(List<String> strategies) {
        List<String> recommendations = new ArrayList<>();
        
        // Analyze current strategy mix
        Map<String, Long> strategyCounts = strategies.stream()
            .collect(Collectors.groupingBy(
                String::toLowerCase, 
                Collectors.counting()
            ));
        
        // Check for strategy diversity
        List<String> allStrategies = List.of(
            "human", "aggressive", "benevolent", "random", "cheater"
        );
        
        // Find missing strategies
        for (String strategy : allStrategies) {
            if (!strategyCounts.containsKey(strategy)) {
                recommendations.add(strategy);
            }
        }
        
        return recommendations;
    }
    
    /**
     * Validates a single game strategy configuration.
     * 
     * @param strategies List of player strategies
     * @return true if the strategy configuration is valid, false otherwise
     */
    public static boolean validateStrategyConfiguration(List<String> strategies) {
        // Check number of players
        if (strategies == null || strategies.size() < 2 || strategies.size() > 5) {
            return false;
        }
        
        // Valid strategy list
        List<String> validStrategies = List.of(
            "human", "aggressive", "benevolent", "random", "cheater"
        );
        
        // Validate each strategy
        return strategies.stream()
            .allMatch(strategy -> 
                strategy != null && 
                validStrategies.contains(strategy.toLowerCase())
            );
    }
}