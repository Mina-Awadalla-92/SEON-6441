package com.Game.Phases;

import com.Game.model.Player;
import com.Game.controller.GameController;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import com.Game.observer.GameLogger;

import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the Issue Order phase of the game.
 * In this phase, players assign orders such as deploying armies to their territories.
 * In the State pattern, this is a concrete state.
 */
public class IssueOrderPhase extends Phase {
	
	/**
	 * Reference to the game logger.
	 */
	private GameLogger d_gameLogger = GameLogger.getInstance();

    /**
     * Set of commands valid in the Issue Order phase.
     */
    private static final Set<String> VALID_COMMANDS = new HashSet<>(Arrays.asList(
        "showmap", "deploy", "advance", "bomb", "blockade", "airlift", "negotiate"
    ));

    /**
     * Starts the Issue Order phase, allowing players to assign orders until they finish or run out of reinforcement armies.
     *
     * @param p_gameController    the game controller handling the game state
     * @param p_players           list of players participating in the game
     * @param p_commandPromptView view handling user interactions through the command prompt
     * @param p_commandParts      the command parts passed as input
     * @param p_gameMap           the game map containing all territories and their connections
     */
    @Override
    public void StartPhase(GameController p_gameController, List<Player> p_players, CommandPromptView p_commandPromptView, String[] p_commandParts, Map p_gameMap) {
        if (d_gameLogger != null) {
            d_gameLogger.logAction("Starting Issue Order Phase");
        }

        for (Player l_player : p_players) {
            boolean l_playerDone = false;
            
            p_gameController.getView().displayPlayerTurn(l_player.getName(), l_player.getNbrOfReinforcementArmies());
            p_gameController.getView().displayPlayerTerritories(l_player.getOwnedTerritories(), l_player, p_gameMap);
            
            while (!l_playerDone) {
                // Check if player has reinforcement armies
                if (l_player.getNbrOfReinforcementArmies() <= 0) {
                    // If no reinforcement armies, ask if they want to issue more orders or finish
                    System.out.println("You have used all your reinforcement armies. Do you want to issue other types of orders?");
                    String l_response = p_commandPromptView.getString("Enter 'yes' to continue or 'no' to finish");
                    if (!l_response.equalsIgnoreCase("yes")) {
                        l_playerDone = true;
                        if (d_gameLogger != null) {
                            d_gameLogger.logAction("Player " + l_player.getName() + " finished issuing orders");
                        }
                        continue;
                    }
                }
                
                String l_orderCommand = p_commandPromptView.getPlayerOrder(
                    l_player.getName(), l_player.getNbrOfReinforcementArmies());
                
                if (l_orderCommand.equalsIgnoreCase("FINISH")) {
                    l_playerDone = true;
                    if (d_gameLogger != null) {
                        d_gameLogger.logAction("Player " + l_player.getName() + " finished issuing orders");
                    }
                    continue;
                }
                
                // Validate command is appropriate for this phase
                String[] l_commandParts = l_orderCommand.split("\\s+");
                if (l_commandParts.length > 0 && !validateCommand(l_commandParts[0])) {
                    p_gameController.getView().displayError(
                        "Invalid command for issue order phase: " + l_commandParts[0]);
                    if (d_gameLogger != null) {
                        d_gameLogger.logAction("Player " + l_player.getName() + " attempted invalid command: " + l_commandParts[0]);
                    }
                    continue;
                }
                
                // Issue the order
                boolean l_success = l_player.issueOrder(l_orderCommand, p_gameMap, p_players);
                
                if (!l_success) { 
                    p_gameController.getView().displayError(
                        "Failed to create order. Please check your command format.");
                    if (d_gameLogger != null) {
                        d_gameLogger.logAction("Player " + l_player.getName() + " failed to issue order: " + l_orderCommand);
                    }
                } else {
                    if (d_gameLogger != null) {
                        d_gameLogger.logAction("Player " + l_player.getName() + " issued order: " + l_orderCommand);
                    }
                }
            }
        }
        
        if (d_gameLogger != null) {
            d_gameLogger.logAction("Issue Order Phase completed");
        }
    }
    
    /**
     * Gets the next phase type in the game flow.
     * 
     * @return the order execution phase as the next phase
     */
    @Override
    public PhaseType getNextPhase() {
        return PhaseType.ORDER_EXECUTION;
    }
    
    /**
     * Checks if a command is valid for the Issue Order phase.
     * 
     * @param p_command the command to validate
     * @return true if the command is valid for this phase, false otherwise
     */
    @Override
    public boolean validateCommand(String p_command) {
        return VALID_COMMANDS.contains(p_command.toLowerCase());
    }
}