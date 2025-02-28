package com.Game.controller;

import com.Game.model.game.GameState;
import com.Game.view.GameView;
import com.Game.model.player.Player;
import com.Game.util.GameConstants;
import com.Game.model.map.Territory;
import com.Game.model.order.Order;
import java.util.List;

/**
 * Controller for main game phase commands.
 */
public class GamePlayController {
    private GameState d_gameState;
    private GameView d_gameView;
    
    /**
     * Constructor initializing the game play controller.
     * 
     * @param p_gameState The game state.
     * @param p_gameView The game view.
     */
    public GamePlayController(GameState p_gameState, GameView p_gameView) {
        this.d_gameState = p_gameState;
        this.d_gameView = p_gameView;
    }
    
    /**
     * Handles commands for the main game phase.
     * 
     * @param p_commandParts The parts of the command.
     */
    public void handleCommand(String[] p_commandParts) {
        if (p_commandParts.length == 0) {
            return;
        }
        
        String l_command = p_commandParts[0];
        
        if (!d_gameState.isGameStarted()) {
            d_gameView.showMessage("Game has not started yet. Use 'startgame' command.");
            return;
        }
        
        switch (l_command) {
            case GameConstants.CMD_SHOWMAP:
                handleShowMap();
                break;
            case GameConstants.CMD_REINFORCEMENT:
                handleReinforcement();
                break;
            case GameConstants.CMD_ISSUEORDER:
                handleIssueOrder();
                break;
            case GameConstants.CMD_EXECUTEORDERS:
                handleExecuteOrders();
                break;
            case GameConstants.CMD_ENDTURN:
                handleEndTurn();
                break;
            default:
                d_gameView.showMessage("Unknown command for Game Play: " + l_command);
        }
    }
    
    /**
     * Displays the current map with game information.
     * Format: showmap
     */
    private void handleShowMap() {
        d_gameView.showMap(d_gameState.getGameMap().toString());
        
        d_gameView.showMessage("\nPlayers status:");
        List<Player> l_players = d_gameState.getPlayers();
        
        for (Player l_player : l_players) {
            d_gameView.showMessage(l_player.getName() + " - Territories: " + l_player.getOwnedTerritories().size() + 
                               ", Reinforcements: " + l_player.getNbrOfReinforcementArmies());
            
            for (Territory l_territory : l_player.getOwnedTerritories()) {
                d_gameView.showMessage("  " + l_territory.getName() + " (" + l_territory.getNumOfArmies() + " armies)");
            }
        }
    }
    
    /**
     * Handles the reinforcement phase of the game.
     * Calculates and assigns reinforcement armies for each player.
     * Format: reinforcement
     */
    public void handleReinforcement() {
        d_gameView.showMessage("\n===== REINFORCEMENT PHASE =====");
        List<Player> l_players = d_gameState.getPlayers();
        
        // Calculate reinforcements for each player
        for (Player l_player : l_players) {
            // Basic calculation: number of territories divided by 3, minimum 3
            int l_reinforcements = Math.max(3, l_player.getOwnedTerritories().size() / 3);
            
            l_player.setNbrOfReinforcementArmies(l_reinforcements);
            d_gameView.showMessage(l_player.getName() + " receives " + l_reinforcements + " reinforcement armies.");
        }
        
        d_gameView.showMessage("\nEntering Issue Order Phase. Use 'issueorder' command to issue orders.");
    }
    
    /**
     * Handles the issue order phase of the game.
     * Allows each player to issue deploy orders.
     * Format: issueorder
     */
    public void handleIssueOrder() {
        d_gameView.showMessage("\n===== ISSUE ORDERS PHASE =====");
        List<Player> l_players = d_gameState.getPlayers();
        
        for (Player l_player : l_players) {
            if (l_player.getNbrOfReinforcementArmies() > 0) {
                d_gameView.showMessage("\n" + l_player.getName() + "'s turn (" + 
                                  l_player.getNbrOfReinforcementArmies() + " reinforcements left)");
                
                // Display player's territories
                d_gameView.showMessage("Your territories:");
                List<Territory> l_playerTerritories = l_player.getOwnedTerritories();
                for (int i = 0; i < l_playerTerritories.size(); i++) {
                    Territory t = l_playerTerritories.get(i);
                    d_gameView.showMessage((i+1) + ". " + t.getName() + " (" + t.getNumOfArmies() + " armies)");
                }
                
                l_player.issueOrder();
            }
        }
        
        d_gameView.showMessage("\nAll players have issued their orders.");
        d_gameView.showMessage("Use 'executeorders' command to execute all orders.");
    }
    
    /**
     * Handles the execute orders phase of the game.
     * Executes all orders in round-robin fashion.
     * Format: executeorders
     */
    public void handleExecuteOrders() {
        d_gameView.showMessage("\n===== EXECUTE ORDERS PHASE =====");
        d_gameView.showMessage("Executing all orders...");
        List<Player> l_players = d_gameState.getPlayers();
        
        // Loop until all orders are executed
        boolean l_ordersRemaining = true;
        
        while (l_ordersRemaining) {
            l_ordersRemaining = false;
            
            // Each player executes one order
            for (Player l_player : l_players) {
                Order l_nextOrder = l_player.nextOrder();
                
                if (l_nextOrder != null) {
                    d_gameView.showMessage("\nExecuting order from " + l_player.getName() + ":");
                    l_nextOrder.execute();
                    l_ordersRemaining = true;
                }
            }
        }
        
        d_gameView.showMessage("\nAll orders executed. Use 'endturn' to end the turn or 'showmap' to see the current state.");
    }
    
    /**
     * Handles the end of a turn.
     * Checks for a winner and either ends the game or starts a new turn.
     * Format: endturn
     */
    public void handleEndTurn() {
        // Check for game end condition
        Player l_winner = checkForWinner();
        
        if (l_winner != null) {
            d_gameView.showMessage("\n*******************************");
            d_gameView.showMessage("Game Over! " + l_winner.getName() + " wins!");
            d_gameView.showMessage("*******************************\n");
            
            d_gameState.setGameStarted(false);
            d_gameState.setCurrentPhase(GameState.MAP_EDITING_PHASE);
        } else {
            // Start a new turn with reinforcement phase
            d_gameView.showMessage("\nTurn ended. Starting new turn.");
            handleReinforcement();
        }
    }
    
    /**
     * Checks if there is a winner in the current game state.
     * 
     * @return The winning player, or null if there is no winner yet
     */
    private Player checkForWinner() {
        List<Player> l_players = d_gameState.getPlayers();
        
        // Check if any player owns all territories
        for (Player l_player : l_players) {
            if (l_player.getOwnedTerritories().size() == d_gameState.getGameMap().getTerritoryList().size()) {
                return l_player;
            }
        }
        
        // Check if only one player has territories
        int l_playersWithTerritories = 0;
        Player l_lastPlayerWithTerritories = null;
        
        for (Player l_player : l_players) {
            if (l_player.getOwnedTerritories().size() > 0) {
                l_playersWithTerritories++;
                l_lastPlayerWithTerritories = l_player;
            }
        }
        
        if (l_playersWithTerritories == 1) {
            return l_lastPlayerWithTerritories;
        }
        
        return null; // No winner yet
    }
}