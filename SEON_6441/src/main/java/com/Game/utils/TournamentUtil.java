package com.Game.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Game.model.AggressivePlayer;
import com.Game.model.BenevolentPlayer;
import com.Game.model.CheaterPlayer;
import com.Game.model.Player;
import com.Game.model.RandomPlayer;
import com.Game.observer.GameLogger;

/**
 * Utility class for tournament-related operations.
 * Provides helper methods for player creation, result tracking, and formatting.
 */
public class TournamentUtil {
    
    /**
     * Creates a player with the specified strategy.
     *
     * @param p_strategy The player strategy name
     * @param p_name The player name
     * @return A new Player object with the appropriate strategy
     */
    public static Player createPlayerByStrategy(String p_strategy, String p_name) {
        switch (p_strategy.toLowerCase()) {
            case "aggressive":
                return new AggressivePlayer(p_name);
            case "benevolent":
                return new BenevolentPlayer(p_name);
            case "random":
                return new RandomPlayer(p_name);
            case "cheater":
                return new CheaterPlayer(p_name);
            default:
                throw new IllegalArgumentException("Invalid player strategy: " + p_strategy);
        }
    }
    
    /**
     * Validates if a strategy name is valid.
     * 
     * @param p_strategy The strategy name to check
     * @return true if valid, false otherwise
     */
    public static boolean isValidStrategy(String p_strategy) {
        return p_strategy.equalsIgnoreCase("aggressive") ||
               p_strategy.equalsIgnoreCase("benevolent") ||
               p_strategy.equalsIgnoreCase("random") ||
               p_strategy.equalsIgnoreCase("cheater");
    }
    
    /**
     * Creates a formatted table from tournament results.
     * 
     * @param p_results Map of tournament results
     * @param p_numberOfGames Number of games played per map
     * @return Formatted table as string
     */
    public static String formatResultsTable(Map<String, Map<Integer, String>> p_results, int p_numberOfGames) {
        StringBuilder table = new StringBuilder();
        
        // Calculate column widths
        int mapNameWidth = 15; // Minimum width
        for (String mapFile : p_results.keySet()) {
            String mapName = extractMapName(mapFile);
            mapNameWidth = Math.max(mapNameWidth, mapName.length() + 2);
        }
        
        int resultWidth = 10; // Minimum width
        
        // Create header row
        table.append(String.format("%-" + mapNameWidth + "s", "Map"));
        for (int i = 1; i <= p_numberOfGames; i++) {
            table.append(String.format("%-" + resultWidth + "s", "Game " + i));
        }
        table.append("\n");
        
        // Add divider
        for (int i = 0; i < mapNameWidth + (resultWidth * p_numberOfGames); i++) {
            table.append("-");
        }
        table.append("\n");
        
        // Add data rows
        for (String mapFile : p_results.keySet()) {
            String mapName = extractMapName(mapFile);
            table.append(String.format("%-" + mapNameWidth + "s", mapName));
            
            Map<Integer, String> mapResults = p_results.get(mapFile);
            for (int gameNumber = 1; gameNumber <= p_numberOfGames; gameNumber++) {
                String winner = mapResults.getOrDefault(gameNumber, "N/A");
                table.append(String.format("%-" + resultWidth + "s", winner));
            }
            table.append("\n");
        }
        
        return table.toString();
    }
    
    /**
     * Extracts a clean map name from a file path.
     * 
     * @param p_mapFile Map file path
     * @return Clean map name
     */
    public static String extractMapName(String p_mapFile) {
        String mapName = p_mapFile;
        if (p_mapFile.contains("/")) {
            mapName = p_mapFile.substring(p_mapFile.lastIndexOf('/') + 1);
        } else if (p_mapFile.contains("\\")) {
            mapName = p_mapFile.substring(p_mapFile.lastIndexOf('\\') + 1);
        }
        if (mapName.toLowerCase().endsWith(".map")) {
            mapName = mapName.substring(0, mapName.length() - 4);
        }
        return mapName;
    }
    
    /**
     * Counts the number of wins for each player strategy.
     * 
     * @param p_results Tournament results
     * @param p_playerStrategies List of player strategies
     * @return Map of strategy names to win counts
     */
    public static Map<String, Integer> countWins(Map<String, Map<Integer, String>> p_results, List<String> p_playerStrategies) {
        Map<String, Integer> winCounts = new HashMap<>();
        
        // Initialize counts for all strategies
        for (String strategy : p_playerStrategies) {
            winCounts.put(strategy, 0);
        }
        winCounts.put("Draw", 0);
        
        // Count wins
        for (Map<Integer, String> mapResults : p_results.values()) {
            for (String winner : mapResults.values()) {
                // Extract the base strategy name from the winner
                String baseStrategy = winner;
                if (winner.contains("_")) {
                    baseStrategy = winner.substring(0, winner.indexOf("_"));
                }
                
                // Increment the count
                if (winCounts.containsKey(baseStrategy)) {
                    winCounts.put(baseStrategy, winCounts.get(baseStrategy) + 1);
                } else if (baseStrategy.equals("Draw")) {
                    winCounts.put("Draw", winCounts.get("Draw") + 1);
                }
            }
        }
        
        return winCounts;
    }
    
    /**
     * Logs tournament results.
     * 
     * @param p_logger Game logger instance
     * @param p_mapFiles List of map files
     * @param p_playerStrategies List of player strategies
     * @param p_numberOfGames Number of games per map
     * @param p_maxTurns Maximum turns per game
     * @param p_results Tournament results
     */
    public static void logTournamentResults(GameLogger p_logger, List<String> p_mapFiles, 
                                           List<String> p_playerStrategies, int p_numberOfGames, 
                                           int p_maxTurns, Map<String, Map<Integer, String>> p_results) {
        if (p_logger == null) {
            return;
        }
        
        StringBuilder log = new StringBuilder("Tournament results:\n");
        log.append("Maps: ").append(String.join(", ", p_mapFiles)).append("\n");
        log.append("Player strategies: ").append(String.join(", ", p_playerStrategies)).append("\n");
        log.append("Games per map: ").append(p_numberOfGames).append("\n");
        log.append("Max turns per game: ").append(p_maxTurns).append("\n\n");
        
        log.append(formatResultsTable(p_results, p_numberOfGames));
        
        Map<String, Integer> winCounts = countWins(p_results, p_playerStrategies);
        log.append("\nWin Statistics:\n");
        for (String strategy : winCounts.keySet()) {
            log.append(strategy).append(": ").append(winCounts.get(strategy)).append(" wins\n");
        }
        
        p_logger.logAction(log.toString());
    }
}