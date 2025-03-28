package com.Game.Phases;

import com.Game.controller.GameController;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;

import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the Startup phase of the game.
 * This phase is responsible for initializing game settings, assigning players, and preparing the game state before gameplay begins.
 * In the State pattern, this is a concrete state.
 */
public class StartupPhase extends Phase {

    /**
     * Set of commands valid in the Startup phase.
     */
    private static final Set<String> VALID_COMMANDS = new HashSet<>(Arrays.asList(
        "showmap", "gameplayer", "assigncountries", "startgame", "editmap", "loadmap"
    ));

    /**
     * Starts the Startup phase with the provided game controller, players, and command prompt view.
     * This method will handle game setup, such as initializing players and allocating resources.
     *
     * @param p_gameController    the game controller handling the game state
     * @param p_players           list of players participating in the game
     * @param p_commandPromptView view handling user interactions through the command prompt
     * @param p_commandParts      the command parts passed as input
     * @param p_gameMap           the game map containing the territories
     */
    @Override
    public void StartPhase(GameController p_gameController, List<Player> p_players, CommandPromptView p_commandPromptView, String[] p_commandParts, Map p_gameMap) {
        if (p_commandParts != null && p_commandParts.length >= 3) {
            String l_action = p_commandParts[1];
            String l_playerName = p_commandParts[2];

            p_gameController.handleGamePlayer(l_action, l_playerName);
            
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Player management: " + l_action + " " + l_playerName);
            }
        } else if (p_commandParts != null && p_commandParts.length > 0) {
            if (p_commandParts[0].equals("startgame")) {
                // Transition to reinforcement phase (which will be followed by issue order phase)
                p_gameController.setCurrentPhase(GameController.MAIN_GAME_PHASE);
                
                if (d_gameLogger != null) {
                    d_gameLogger.logAction("Game started. Moving to main game phase.");
                }
            }
        } else {
            p_gameController.getView().displayError("Usage: gameplayer -add playerName OR gameplayer -remove playerName");
            
            if (d_gameLogger != null) {
                d_gameLogger.logAction("Error: Invalid gameplayer command format");
            }
        }
    }
    
    /**
     * Gets the next phase type in the game flow.
     * 
     * @return the issue order phase as the next phase
     */
    @Override
    public PhaseType getNextPhase() {
        return PhaseType.ISSUE_ORDER;
    }
    
    /**
     * Checks if a command is valid for the Startup phase.
     * 
     * @param p_command the command to validate
     * @return true if the command is valid for this phase, false otherwise
     */
    @Override
    public boolean validateCommand(String p_command) {
        return VALID_COMMANDS.contains(p_command.toLowerCase());
    }
}