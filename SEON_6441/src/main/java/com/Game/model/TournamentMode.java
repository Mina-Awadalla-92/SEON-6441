package com.Game.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import com.Game.controller.GameController;
import com.Game.observer.GameLogger;
import com.Game.utils.MapLoader;

/**
 * Represents a tournament mode for the Warzone game.
 * A tournament runs multiple games across different maps with different player strategies.
 * The results are tracked and displayed at the end of the tournament.
 */
public class TournamentMode {
    
    /**
     * List of map file paths to be used in the tournament.
     */
    private List<String> d_mapFiles;
    
    /**
     * List of player strategies to be used in the tournament.
     */
    private List<String> d_playerStrategies;
    
    /**
     * Number of games to be played on each map.
     */
    private int d_numberOfGames;
    
    /**
     * Maximum number of turns for each game.
     */
    private int d_maxTurns;
    
    /**
     * Reference to the game controller.
     */
    private GameController d_gameController;
    
    /**
     * Stores the results of the tournament.
     * Map<MapName, Map<GameNumber, WinnerName>>
     */
    private java.util.Map<String, java.util.Map<Integer, String>> d_results;
    
    /**
     * Game logger for logging tournament events.
     */
    private GameLogger d_gameLogger;
    
    /**
     * Constructor initializing the tournament with the specified parameters.
     *
     * @param p_mapFiles List of map file paths
     * @param p_playerStrategies List of player strategies
     * @param p_numberOfGames Number of games per map
     * @param p_maxTurns Maximum number of turns per game
     * @param p_gameController Reference to the game controller
     */
    public TournamentMode(List<String> p_mapFiles, List<String> p_playerStrategies, 
                         int p_numberOfGames, int p_maxTurns, GameController p_gameController) {
        this.d_mapFiles = p_mapFiles;
        this.d_playerStrategies = p_playerStrategies;
        this.d_numberOfGames = p_numberOfGames;
        this.d_maxTurns = p_maxTurns;
        this.d_gameController = p_gameController;
        this.d_results = new HashMap<>();
        this.d_gameLogger = GameLogger.getInstance();
    }
    
    /**
     * Runs the tournament, playing the specified number of games on each map.
     * Records the results for later display.
     */
    public void runTournament() {
        if (d_gameLogger != null) {
            d_gameLogger.logAction("Starting tournament with " + d_mapFiles.size() + " maps, " + 
                                  d_playerStrategies.size() + " player strategies, " + 
                                  d_numberOfGames + " games per map, and " + 
                                  d_maxTurns + " max turns per game");
        }
        
        // Set up initial message
        System.out.println("\n========== TOURNAMENT MODE ==========");
        System.out.println("Maps: " + String.join(", ", d_mapFiles));
        System.out.println("Player Strategies: " + String.join(", ", d_playerStrategies));
        System.out.println("Games per Map: " + d_numberOfGames);
        System.out.println("Max Turns per Game: " + d_maxTurns);
        System.out.println("Running tournament...");
        
        // First validate all maps
        List<String> validMaps = validateMaps();
        if (validMaps.isEmpty()) {
            System.out.println("\nError: No valid maps found. Tournament cannot proceed.");
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Tournament aborted: No valid maps found");
            }
            return;
        }
        
        if (validMaps.size() < d_mapFiles.size()) {
            System.out.println("\nWarning: Some maps are invalid and will be skipped.");
            System.out.println("Proceeding with valid maps: " + String.join(", ", validMaps));
        }
        
        for (String mapFile : validMaps) {
            // Create result tracking for this map
            d_results.put(mapFile, new HashMap<>());
            
            System.out.println("\nRunning games on map: " + mapFile);
            
            for (int gameNumber = 1; gameNumber <= d_numberOfGames; gameNumber++) {
                System.out.println("  Playing game " + gameNumber + "...");
                
                // Play a single game on this map
                String winner = playSingleGame(mapFile, gameNumber);
                
                // Store the result
                d_results.get(mapFile).put(gameNumber, winner);
                
                System.out.println("  Game " + gameNumber + " result: " + winner);

            }
        }
        
        System.out.println("\nTournament completed!");
    }

    /**
     * Validates all maps in the map files list with improved error reporting.
     * 
     * @return A list of valid map files
     */
    private List<String> validateMaps() {
        List<String> validMaps = new ArrayList<>();
        List<String> invalidMaps = new ArrayList<>();
        MapLoader mapValidator = new MapLoader();
        
        System.out.println("\n===== MAP VALIDATION =====");
        
        for (String mapFile : d_mapFiles) {
            System.out.println("\nValidating map: " + mapFile);
            
            // Reset the map loader for each map to ensure independent validation
            mapValidator = new MapLoader();
            
            // Check if map exists
            BufferedReader reader = mapValidator.isMapExist(mapFile);
            if (reader == null) {
                System.out.println("  ❌ Error: Map file not found");
                if (d_gameLogger != null) {
                    d_gameLogger.logAction("Map validation failed: File not found - " + mapFile);
                }
                invalidMaps.add(mapFile);
                continue;
            }
            
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Check map format
            boolean isValidFormat = mapValidator.isValid(mapFile);
            if (!isValidFormat) {
                System.out.println("  ❌ Error: Invalid map format");
                if (d_gameLogger != null) {
                    d_gameLogger.logAction("Map validation failed: Invalid format - " + mapFile);
                }
                invalidMaps.add(mapFile);
                continue;
            }
            
            // Load and validate map connectivity
            mapValidator.read(mapFile);
            boolean isValidMap = mapValidator.validateMap(false);
            if (!isValidMap) {
                System.out.println("  ❌ Error: Map validation failed (connectivity or other issues)");
                if (d_gameLogger != null) {
                    d_gameLogger.logAction("Map validation failed: Invalid map structure - " + mapFile);
                }
                invalidMaps.add(mapFile);
                continue;
            }
            
            System.out.println("  ✓ Map is valid");
            validMaps.add(mapFile);
        }
        
        // Print validation summary
        System.out.println("\n===== VALIDATION SUMMARY =====");
        System.out.println("Total maps: " + d_mapFiles.size());
        System.out.println("Valid maps: " + validMaps.size());
        System.out.println("Invalid maps: " + invalidMaps.size());
        
        if (!invalidMaps.isEmpty()) {
            System.out.println("\nInvalid maps that will be skipped:");
            for (String map : invalidMaps) {
                System.out.println("  - " + map);
            }
        }
        
        System.out.println();
        
        return validMaps;
    }

    /**
     * Displays the tournament results in a formatted table.
     */
    public void displayResults() {
        System.out.println("\n=============== Tournament Results ===============");
        System.out.println("M: " + String.join(", ", d_mapFiles));
        System.out.println("P: " + String.join(", ", d_playerStrategies));
        System.out.println("G: " + d_numberOfGames);
        System.out.println("D: " + d_maxTurns);
        System.out.println();
        
        // Calculate column widths for better formatting
        int mapNameWidth = 15; // Minimum width for map names
        int resultWidth = 10;  // Minimum width for game results
        
        // Find the longest map name
        for (String mapFile : d_results.keySet()) {
            String mapName = extractMapName(mapFile);
            mapNameWidth = Math.max(mapNameWidth, mapName.length() + 2);
        }
        
        // Create header row with better formatting
        String headerFormat = "%-" + mapNameWidth + "s";
        System.out.printf(headerFormat, "Map");
        
        for (int i = 1; i <= d_numberOfGames; i++) {
            System.out.printf("%-" + resultWidth + "s", "Game " + i);
        }
        System.out.println();
        
        // Add a divider line
        printDivider(mapNameWidth + (resultWidth * d_numberOfGames));
        
        // Display results for each map with better formatting
        String rowFormat = "%-" + mapNameWidth + "s";
        for (String mapFile : d_results.keySet()) {
            String mapName = extractMapName(mapFile);
            System.out.printf(rowFormat, mapName);
            
            // Display results for each game on this map
            java.util.Map<Integer, String> mapResults = d_results.get(mapFile);
            for (int gameNumber = 1; gameNumber <= d_numberOfGames; gameNumber++) {
                String winner = mapResults.getOrDefault(gameNumber, "N/A");
                System.out.printf("%-" + resultWidth + "s", winner);
            }
            System.out.println();
        }
        
        // Add another divider line
        printDivider(mapNameWidth + (resultWidth * d_numberOfGames));
        
        // Display winning stats
        System.out.println("\nWinning Statistics:");
        
        java.util.Map<String, Integer> winCounts = countWins();
        for (String player : winCounts.keySet()) {
            System.out.println(player + ": " + winCounts.get(player) + " wins");
        }
        
        System.out.println("=================================================");
        
        // Log the tournament results
        if (d_gameLogger != null) {
            StringBuilder logSB = new StringBuilder("Tournament results:\n");
            logSB.append("Maps: ").append(String.join(", ", d_mapFiles)).append("\n");
            logSB.append("Player strategies: ").append(String.join(", ", d_playerStrategies)).append("\n");
            logSB.append("Games per map: ").append(d_numberOfGames).append("\n");
            logSB.append("Max turns per game: ").append(d_maxTurns).append("\n\n");
            
            for (String mapFile : d_results.keySet()) {
                String mapName = extractMapName(mapFile);
                logSB.append("Map ").append(mapName).append(" results: ");
                
                java.util.Map<Integer, String> mapResults = d_results.get(mapFile);
                for (int gameNumber = 1; gameNumber <= d_numberOfGames; gameNumber++) {
                    String winner = mapResults.getOrDefault(gameNumber, "N/A");
                    logSB.append("Game ").append(gameNumber).append(": ").append(winner).append(", ");
                }
                logSB.append("\n");
            }
            
            d_gameLogger.logAction(logSB.toString());
        }
    }

    /**
     * Extracts a user-friendly map name from the file path.
     * 
     * @param mapFile The map file path
     * @return A user-friendly map name
     */
    private String extractMapName(String mapFile) {
        String mapName = mapFile;
        if (mapFile.contains("/")) {
            mapName = mapFile.substring(mapFile.lastIndexOf('/') + 1);
        } else if (mapFile.contains("\\")) {
            mapName = mapFile.substring(mapFile.lastIndexOf('\\') + 1);
        }
        if (mapName.toLowerCase().endsWith(".map")) {
            mapName = mapName.substring(0, mapName.length() - 4);
        }
        return mapName;
    }

    /**
     * Prints a divider line of specified width.
     * 
     * @param width The width of the divider line
     */
    private void printDivider(int width) {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Counts the number of wins for each player strategy.
     * 
     * @return A map of player names to win counts
     */
    private java.util.Map<String, Integer> countWins() {
        java.util.Map<String, Integer> winCounts = new HashMap<>();
        
        // Initialize win counts for all player strategies and Draw
        for (String strategy : d_playerStrategies) {
            winCounts.put(strategy, 0);
        }
        winCounts.put("Draw", 0);
        
        // Count wins from results
        for (java.util.Map<Integer, String> mapResults : d_results.values()) {
            for (String winner : mapResults.values()) {
                // Extract the base strategy name from the winner
                String baseStrategy = winner;
                if (winner.contains("_")) {
                    baseStrategy = winner.substring(0, winner.indexOf("_"));
                }
                
                // Increment the count for the appropriate strategy
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
     * Plays a single game as part of the tournament.
     *
     * @param p_mapFile The map file to play on
     * @param p_gameNumber The game number (for logging)
     * @return The name of the winner, or "Draw" if no winner after max turns
     */
    private String playSingleGame(String p_mapFile, int p_gameNumber) {
        if (d_gameLogger != null) {
            d_gameLogger.logAction("Starting game " + p_gameNumber + " on map " + p_mapFile);
        }
        
        // Reset the game state
        MapLoader l_mapLoader = new MapLoader();
        
        // Load the map
        l_mapLoader.resetLoadedMap();
        
        // Try to load the map file
        BufferedReader l_reader = l_mapLoader.isMapExist(p_mapFile);
        if (l_reader == null) {
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Failed to find map file: " + p_mapFile);
            }
            return "Map Error";
        }
        
        try {
            l_reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        boolean l_isMapValid = l_mapLoader.isValid(p_mapFile);
        if (!l_isMapValid) {
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Invalid map format: " + p_mapFile);
            }
            return "Invalid Map";
        }
        
        l_mapLoader.read(p_mapFile);
        com.Game.model.Map l_gameMap = l_mapLoader.getLoadedMap();
        
        if (!l_mapLoader.validateMap(false)) {
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Map validation failed: " + p_mapFile);
            }
            return "Invalid Map";
        }
        
        // Create players based on strategies
        List<Player> l_players = new ArrayList<>();
        for (int i = 0; i < d_playerStrategies.size(); i++) {
            String strategy = d_playerStrategies.get(i);
            Player player = createPlayerByStrategy(strategy, strategy + "_" + (i+1));
            l_players.add(player);
        }
        
        if (l_players.size() < 2) {
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Not enough players for a game");
            }
            return "Not Enough Players";
        }
        
        // Assign countries randomly
        assignCountriesRandomly(l_gameMap, l_players);
        
        // Run the game for up to max turns or until a winner is found
        int currentTurn = 0;
        Player winner = null;
        
        while (currentTurn < d_maxTurns && winner == null) {
            // Reinforcement phase
            calculateReinforcements(l_players);
            
            // Issue orders phase
            issueOrders(l_players, l_gameMap);
            
            // Execute orders phase
            executeOrders(l_players);
            
            // Check for a winner
            winner = checkForWinner(l_gameMap, l_players);
            
            // Reset players' status for next turn
            for (Player player : l_players) {
                player.setHasConqueredThisTurn(false);
                player.setNegociatedPlayersPerTurn(new ArrayList<>());
            }
            
            currentTurn++;
        }
        
        if (winner != null) {
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Game " + p_gameNumber + " on map " + p_mapFile + 
                                      " ended with winner: " + winner.getName());
            }
            return winner.getName();
        } else {
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Game " + p_gameNumber + " on map " + p_mapFile + 
                                      " ended in a draw after " + d_maxTurns + " turns");
            }
            return "Draw";
        }
    }
    
    /**
     * Assigns countries randomly to players.
     *
     * @param p_gameMap The game map
     * @param p_players List of players
     */
    private void assignCountriesRandomly(com.Game.model.Map p_gameMap, List<Player> p_players) {
        List<Territory> l_territories = p_gameMap.getTerritoryList();
        
        if (l_territories.isEmpty() || p_players.isEmpty()) {
            return;
        }
        
        // Shuffle territories for random assignment
        java.util.Random rand = new java.util.Random();
        for (int i = l_territories.size() - 1; i > 0; i--) {
            int l_index = rand.nextInt(i + 1);
            Territory l_temp = l_territories.get(l_index);
            l_territories.set(l_index, l_territories.get(i));
            l_territories.set(i, l_temp);
        }
        
        // Clear any existing ownership
        for (Player l_player : p_players) {
            l_player.getOwnedTerritories().clear();
        }
        
        // Assign territories to players
        int l_playerCount = p_players.size();
        for (int i = 0; i < l_territories.size(); i++) {
            Territory l_territory = l_territories.get(i);
            Player l_player = p_players.get(i % l_playerCount);
            
            l_territory.setOwner(l_player);
            l_player.addTerritory(l_territory);
            
            // Set initial armies (e.g., 1 per territory)
            l_territory.setNumOfArmies(1);
        }
    }
    
    /**
     * Calculate reinforcements for each player.
     *
     * @param p_players List of players
     */
    private void calculateReinforcements(List<Player> p_players) {
        for (Player player : p_players) {
            // Basic calculation: number of territories divided by 3, minimum 3
            int reinforcements = Math.max(3, player.getOwnedTerritories().size() / 3);
            player.setNbrOfReinforcementArmies(reinforcements);
        }
    }
    
    /**
     * Issue orders for all players.
     *
     * @param p_players List of players
     * @param p_gameMap The game map
     */
    private void issueOrders(List<Player> p_players, com.Game.model.Map p_gameMap) {
        for (Player player : p_players) {
            player.issueOrder("", p_gameMap, p_players);
        }
    }
    
    /**
     * Execute orders for all players.
     *
     * @param p_players List of players
     */
    private void executeOrders(List<Player> p_players) {
        boolean ordersRemaining = true;
        
        while (ordersRemaining) {
            ordersRemaining = false;
            
            for (Player player : p_players) {
                com.Game.model.order.Order nextOrder = player.nextOrder();
                
                if (nextOrder != null) {
                    nextOrder.execute();
                    ordersRemaining = true;
                }
            }
        }
    }
    
    /**
     * Check if there is a winner in the current game state.
     *
     * @param p_gameMap The game map
     * @param p_players List of players
     * @return The winning player, or null if there is no winner yet
     */
    private Player checkForWinner(com.Game.model.Map p_gameMap, List<Player> p_players) {
        // Check if any player owns all territories
        int totalTerritories = p_gameMap.getTerritoryList().size();
        for (Player player : p_players) {
            if (player.getOwnedTerritories().size() == totalTerritories) {
                return player;
            }
        }
        
        // Check if only one player has territories
        int playersWithTerritories = 0;
        Player lastPlayerWithTerritories = null;
        
        for (Player player : p_players) {
            if (player.getOwnedTerritories().size() > 0) {
                playersWithTerritories++;
                lastPlayerWithTerritories = player;
            }
        }
        
        if (playersWithTerritories == 1) {
            return lastPlayerWithTerritories;
        }
        
        return null; // No winner yet
    }
    
    /**
     * Creates a player with the specified strategy.
     *
     * @param p_strategy The player strategy
     * @param p_name The player name
     * @return A new Player object with the appropriate strategy
     */
    private Player createPlayerByStrategy(String p_strategy, String p_name) {
        switch (p_strategy.toLowerCase()) {
            case "aggressive":
                return new AggressivePlayer(p_name, "aggressive");
            case "benevolent":
                return new BenevolentPlayer(p_name, "benevolent");
            case "random":
                return new RandomPlayer(p_name, "random");
            case "cheater":
                return new CheaterPlayer(p_name, "cheater");
            default:
                // Default to human player if strategy not recognized
                return new HumanPlayer(p_name, "human");
        }
    }
//    
//    /**
//     * Displays the tournament results in a formatted table.
//     */
//    public void displayResults() {
//        System.out.println("\n=============== Tournament Results ===============");
//        System.out.println("M: " + String.join(", ", d_mapFiles));
//        System.out.println("P: " + String.join(", ", d_playerStrategies));
//        System.out.println("G: " + d_numberOfGames);
//        System.out.println("D: " + d_maxTurns);
//        System.out.println();
//        
//        // Create header row
//        System.out.print("\t\t");
//        for (int i = 1; i <= d_numberOfGames; i++) {
//            System.out.print("Game " + i + "\t");
//        }
//        System.out.println();
//        
//        // Display results for each map
//        for (String mapFile : d_mapFiles) {
//            // Extract the map name from the file path
//            String mapName = mapFile;
//            if (mapFile.contains("/")) {
//                mapName = mapFile.substring(mapFile.lastIndexOf('/') + 1);
//            } else if (mapFile.contains("\\")) {
//                mapName = mapFile.substring(mapFile.lastIndexOf('\\') + 1);
//            }
//            if (mapName.toLowerCase().endsWith(".map")) {
//                mapName = mapName.substring(0, mapName.length() - 4);
//            }
//            
//            System.out.print(mapName + "\t");
//            
//            // Display results for each game on this map
//            java.util.Map<Integer, String> mapResults = d_results.get(mapFile);
//            for (int gameNumber = 1; gameNumber <= d_numberOfGames; gameNumber++) {
//                String winner = mapResults.get(gameNumber);
//                System.out.print(winner + "\t");
//            }
//            System.out.println();
//        }
//        
//        System.out.println("=================================================");
//        
//        // Log the tournament results
//        if (d_gameLogger != null) {
//            StringBuilder logSB = new StringBuilder("Tournament results:\n");
//            logSB.append("Maps: ").append(String.join(", ", d_mapFiles)).append("\n");
//            logSB.append("Player strategies: ").append(String.join(", ", d_playerStrategies)).append("\n");
//            logSB.append("Games per map: ").append(d_numberOfGames).append("\n");
//            logSB.append("Max turns per game: ").append(d_maxTurns).append("\n");
//            
//            for (String mapFile : d_mapFiles) {
//                String mapName = mapFile;
//                if (mapFile.contains("/")) {
//                    mapName = mapFile.substring(mapFile.lastIndexOf('/') + 1);
//                }
//                if (mapName.toLowerCase().endsWith(".map")) {
//                    mapName = mapName.substring(0, mapName.length() - 4);
//                }
//                
//                logSB.append("Map ").append(mapName).append(" results: ");
//                
//                java.util.Map<Integer, String> mapResults = d_results.get(mapFile);
//                for (int gameNumber = 1; gameNumber <= d_numberOfGames; gameNumber++) {
//                    String winner = mapResults.get(gameNumber);
//                    logSB.append("Game ").append(gameNumber).append(": ").append(winner).append(", ");
//                }
//                logSB.append("\n");
//            }
//            
//            d_gameLogger.logAction(logSB.toString());
//        }
//    }
}