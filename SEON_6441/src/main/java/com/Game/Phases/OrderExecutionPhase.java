package com.Game.Phases;

import com.Game.model.Player;
import com.Game.controller.GameController;
import com.Game.model.order.Order;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import com.Game.observer.GameLogger;

import java.util.List;

/**
 * Represents the Order Execution phase of the game.
 * In this phase, players execute their orders in a sequential manner until all orders are processed.
 * In the Command pattern, this class is part of the Client, retrieving Commands from Invokers and executing them.
 */
public class OrderExecutionPhase extends Phase {

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
        GameLogger l_gameLogger = GameLogger.getInstance();
        
        // Loop until all orders are executed
        boolean l_ordersRemaining = true;

        while (l_ordersRemaining) {
            l_ordersRemaining = false;

            // Each player executes one order in round-robin fashion
            for (Player l_player : p_players) {
                Order l_nextOrder = l_player.nextOrder();

                if (l_nextOrder != null) {
                    p_gameController.getView().displayExecutingOrder(l_player.getName());
                    l_gameLogger.logAction("Executing order from player " + l_player.getName());
                    
                    // Execute the order (Command)
                    l_nextOrder.execute();
                    
                    l_ordersRemaining = true;
                }
            }
        }
    }
}