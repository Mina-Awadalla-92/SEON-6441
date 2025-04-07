package com.Game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.Game.controller.GameController;
import com.Game.observer.GameLogger;
import com.Game.utils.MapLoader;
import com.Game.utils.SingleGameUtil;

/**
 * Represents a single game mode in the Warzone game.
 * This class can run a single game with various player strategies.
 */
public class SingleGameMode {
    
    /**
     * The map file to be used for the game.
     */
    private String d_mapFile;
    
    /**
     * List of player strategies to be used in the game.
     */
    private List<String> d_playerStrategies;
    
    /**
     * Maximum number of turns for the game (used only in tournament mode).
     */
    private int d_maxTurns;
    
    /**
     * Reference to the game controller.
     */
    private GameController d_gameController;
    
    /**
     * Game logger for logging game events.
     */
    private GameLogger d_gameLogger;
    
    /**
     * Random number generator for game mechanics.
     */
    private Random d_random;
    
    
    /**
     * Returns the number of turns taken in the game.
     *
     * @return Number of turns taken
     */
    public int getTurnsTaken() {
        return d_turnsTaken;
    }
    
    /**
     * Tracks the number of turns taken in the game.
     */
    private int d_turnsTaken;
    
    /**
     * Constructor initializing the single game mode.
     *
     * @param p_mapFile The map file to be used
     * @param p_playerStrategies List of player strategies
     * @param p_maxTurns Maximum number of turns (only used in tournament mode)
     * @param p_gameController Reference to the game controller
     */
    public SingleGameMode(String p_mapFile, List<String> p_playerStrategies, 
                          int p_maxTurns, GameController p_gameController) {
        this.d_mapFile = p_mapFile;
        this.d_playerStrategies = p_playerStrategies;
        this.d_maxTurns = p_maxTurns;
        this.d_gameController = p_gameController;
        this.d_gameLogger = GameLogger.getInstance();
        this.d_random = new Random();
        this.d_turnsTaken = 0; // Initialize turn counter
    }
    
    /**
     * Runs a single game and returns the winner.
     *
     * @return The name of the winner, or "Draw" if no winner emerges
     */
    public String runSingleGame() {
        if (d_gameLogger != null) {
            d_gameLogger.logAction("Starting Single Game on map " + d_mapFile);
        }
        
        // Reset and load the map
        MapLoader l_mapLoader = new MapLoader();
        l_mapLoader.resetLoadedMap();
        
        // Load and validate the map
        l_mapLoader.read(d_mapFile);
        Map l_gameMap = l_mapLoader.getLoadedMap();
        
        if (!l_mapLoader.validateMap(false)) {
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Map validation failed: " + d_mapFile);
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
        
        // Check if game has human players
        boolean hasHumanPlayers = l_players.stream()
            .anyMatch(player -> player instanceof HumanPlayer);
        
        // Run the game until a winner is found, without a turn limit for single game mode
        int currentTurn = 0;
        Player winner = null;
        
        while (winner == null) {
            // For tournament mode, obey the max turns limit
            if (!hasHumanPlayers && d_maxTurns > 0 && currentTurn >= d_maxTurns) {
                break;
            }
            
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
            d_turnsTaken = currentTurn; // Update turns taken
        }
        
        if (winner != null) {
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Single Game on map " + d_mapFile + 
                                      " ended with winner: " + winner.getName());
            }
            return winner.getName();
        } else {
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Single Game on map " + d_mapFile + 
                                      " ended in a draw after " + currentTurn + " turns");
            }
            return "Draw";
        }
    }
    
    /**
     * Creates a player with the specified strategy.
     *
     * @param p_strategy The player strategy name
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
    
    /**
     * Assigns countries randomly to players.
     *
     * @param p_gameMap The game map
     * @param p_players List of players
     */
    private void assignCountriesRandomly(Map p_gameMap, List<Player> p_players) {
        List<Territory> l_territories = p_gameMap.getTerritoryList();
        
        if (l_territories.isEmpty() || p_players.isEmpty()) {
            return;
        }
        
        // Shuffle territories for random assignment
        for (int i = l_territories.size() - 1; i > 0; i--) {
            int l_index = d_random.nextInt(i + 1);
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
    private void issueOrders(List<Player> p_players, Map p_gameMap) {
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
    private Player checkForWinner(Map p_gameMap, List<Player> p_players) {
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
}