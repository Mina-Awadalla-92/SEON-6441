package com.Game.controller;

import com.Game.model.game.GameState;
import com.Game.view.GameView;
import com.Game.model.player.Player;
import com.Game.model.map.Territory;
import java.util.List;
import java.util.Random;

/**
 * Controller for game setup phase commands.
 */
public class GameSetupController {
    private GameState d_gameState;
    private GameView d_gameView;
    
    /**
     * Constructor initializing the game setup controller.
     * 
     * @param p_gameState The game state.
     * @param p_gameView The game view.
     */
    public GameSetupController(GameState p_gameState, GameView p_gameView) {
        this.d_gameState = p_gameState;
        this.d_gameView = p_gameView;
    }
    
    /**
     * Handles commands for the game setup phase.
     * 
     * @param p_commandParts The parts of the command.
     */
    public void handleCommand(String[] p_commandParts) {
        if (p_commandParts.length == 0) {
            return;
        }
        
        String l_command = p_commandParts[0];
        
        switch (l_command) {
            case "showmap":
                handleShowMap();
                break;
            case "gameplayer":
                handleGamePlayer(p_commandParts);
                break;
            case "assigncountries":
                handleAssignCountries();
                break;
            case "startgame":
                startMainGame();
                break;
            default:
                d_gameView.showMessage("Unknown command for Game Setup: " + l_command);
        }
    }
    
    /**
     * Displays the current map.
     * Format: showmap
     */
    private void handleShowMap() {
        d_gameView.showMap(d_gameState.getGameMap().toString());
    }
    
    /**
     * Handles the gameplayer command to add or remove players.
     * Format: gameplayer -add playerName -remove playerName
     * 
     * @param p_commandParts Array of command components
     */
    public void handleGamePlayer(String[] p_commandParts) {
        if (p_commandParts.length < 3) {
            d_gameView.showMessage("Usage: gameplayer -add playerName OR gameplayer -remove playerName");
            return;
        }
        
        String l_action = p_commandParts[1];
        String l_playerName = p_commandParts[2];
        List<Player> l_players = d_gameState.getPlayers();
        
        if (l_action.equals("-add")) {
            // Check if player already exists
            boolean l_playerExists = false;
            for (Player l_player : l_players) {
                if (l_player.getName().equals(l_playerName)) {
                    l_playerExists = true;
                    break;
                }
            }
            
            if (!l_playerExists) {
                l_players.add(new Player(l_playerName));
                d_gameView.showMessage("Player added: " + l_playerName);
            } else {
                d_gameView.showMessage("Player already exists: " + l_playerName);
            }
        } else if (l_action.equals("-remove")) {
            l_players.removeIf(player -> player.getName().equals(l_playerName));
            d_gameView.showMessage("Player removed: " + l_playerName);
        } else {
            d_gameView.showMessage("Invalid action for gameplayer.");
        }
    }
    
    /**
     * Handles the assigncountries command to randomly distribute territories among players.
     * Format: assigncountries
     */
    public void handleAssignCountries() {
        List<Player> l_players = d_gameState.getPlayers();
        
        if (l_players.size() < 2) {
            d_gameView.showMessage("Need at least 2 players to start the game.");
            return;
        }
        
        // Get all territories from the map
        List<Territory> l_territories = d_gameState.getGameMap().getTerritoryList();
        
        if (l_territories.isEmpty()) {
            d_gameView.showMessage("No territories in the map. Cannot assign countries.");
            return;
        }
        
        // Shuffle territories for random assignment
        Random l_random = new Random();
        for (int i = l_territories.size() - 1; i > 0; i--) {
            int l_index = l_random.nextInt(i + 1);
            Territory temp = l_territories.get(l_index);
            l_territories.set(l_index, l_territories.get(i));
            l_territories.set(i, temp);
        }
        
        // Clear any existing ownership
        for (Player l_player : l_players) {
            l_player.getOwnedTerritories().clear();
        }
        
        // Assign territories to players
        int l_playerCount = l_players.size();
        for (int i = 0; i < l_territories.size(); i++) {
            Territory l_territory = l_territories.get(i);
            Player l_player = l_players.get(i % l_playerCount);
            
            l_territory.setOwner(l_player);
            l_player.addTerritory(l_territory);
            
            // Set initial armies (e.g., 1 per territory)
            l_territory.setNumOfArmies(1);
        }
        
        d_gameView.showMessage("Countries assigned to players:");
        for (Player l_player : l_players) {
            d_gameView.showMessage(l_player.getName() + " owns " + l_player.getOwnedTerritories().size() + " territories.");
        }
        
        d_gameView.showMessage("Ready to start the game. Use 'startgame' command to begin.");
    }
    
    /**
     * Starts the main game after the startup phase.
     * Format: startgame
     */
    public void startMainGame() {
        List<Player> l_players = d_gameState.getPlayers();
        
        if (l_players.size() < 2) {
            d_gameView.showMessage("Need at least 2 players to start the game.");
            return;
        }
        
        // Initialize the game
        d_gameState.setGameStarted(true);
        d_gameState.setCurrentPhase(GameState.MAIN_GAME_PHASE);
        
        d_gameView.showMessage("Game started! Beginning reinforcement phase.");
    }
}