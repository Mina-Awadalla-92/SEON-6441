package com.Game.Phases;

import com.Game.model.Player;
import com.Game.controller.GameController;
import com.Game.model.order.Order;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import com.Game.observer.GameLogger;

import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the Order Execution phase of the game.
 * In this phase, players execute their orders in a sequential manner until all orders are processed.
 * In the State pattern, this is a concrete state.
 */
public class OrderExecutionPhase extends Phase {

    /**
     * Set of commands valid in the Order Execution phase.
     */
    private static final Set<String> VALID_COMMANDS = new HashSet<>(Arrays.asList(
        "showmap", "endturn"
    ));

    /**
     * Starts the Order Execution phase, where players take turns executing their orders
     * until no orders remain.
     *
     * @param p_gameController    the game controller handling the game state
     * @param p_players           list of players participating in the game
     * @param p_commandPromptView view handling user interactions through the command prompt
     * @param p_commandParts      the command parts passed as input
     * @param p_gameMap           the game map containing the territories
     */
    @Override
    public void StartPhase(GameController p_gameController, List<Player> p_players, CommandPromptView p_commandPromptView, String[] p_commandParts, Map p_gameMap) {
        if (d_gameLogger != null) {
            d_gameLogger.logAction("Starting Order Execution Phase");
        }
        
        // Loop until all orders are executed
        boolean l_ordersRemaining = true;

        while (l_ordersRemaining) {
            l_ordersRemaining = false;

            // Each player executes one order in round-robin fashion
            for (Player l_player : p_players) {
                Order l_nextOrder = l_player.nextOrder();

                if (l_nextOrder != null) {
                    p_gameController.getView().displayExecutingOrder(l_player.getName());
                    if (d_gameLogger != null) {
                        d_gameLogger.logAction("Executing order from player " + l_player.getName());
                    }
                    
                    // Execute the order (Command)
                    l_nextOrder.execute();
                    
                    l_ordersRemaining = true;
                }
            }
        }
        
        if (d_gameLogger != null) {
            d_gameLogger.logAction("Order Execution Phase completed");
        }
    }
    
    /**
     * Gets the next phase type in the game flow.
     * After order execution, the game typically returns to the issue order phase for the next turn.
     * 
     * @return the issue order phase as the next phase
     */
    @Override
    public PhaseType getNextPhase() {
        return PhaseType.ISSUE_ORDER;
    }
    
    /**
     * Checks if a command is valid for the Order Execution phase.
     * 
     * @param p_command the command to validate
     * @return true if the command is valid for this phase, false otherwise
     */
    @Override
    public boolean validateCommand(String p_command) {
        return VALID_COMMANDS.contains(p_command.toLowerCase());
    }
}