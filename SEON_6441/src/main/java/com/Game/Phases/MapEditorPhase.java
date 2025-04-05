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
 * Represents the Map Editor phase of the game.
 * In this phase, the player can create and edit maps.
 * In the State pattern, this is a concrete state.
 */
public class MapEditorPhase extends Phase {

	/**
	 * Set of commands valid in the Map Editor phase.
	 */
	private static final Set<String> VALID_COMMANDS = new HashSet<>(Arrays.asList(
	    "editcontinent", "editcountry", "editneighbor", "showmap", 
	    "savemap", "editmap", "validatemap", "loadmap", "gameplayer", "selectmode"
	));

    /**
     * Starts the Map Editor phase with the provided game controller, players, and command prompt view.
     *
     * @param p_gameController    the game controller handling the game state
     * @param p_players           list of players participating in the game
     * @param p_commandPromptView view handling user interactions through the command prompt
     * @param p_commandParts      the command parts passed as input
     * @param p_gameMap           the game map containing the territories
     */
    @Override
    public void StartPhase(GameController p_gameController, List<Player> p_players, CommandPromptView p_commandPromptView, String[] p_commandParts, Map p_gameMap) {
        if (p_commandParts != null && p_commandParts.length > 0) {
            String l_command = p_commandParts[0];
            
            if (l_command.equals("gameplayer")) {
                // Transition to startup phase
                p_gameController.setCurrentPhase(GameController.STARTUP_PHASE);
                Phase l_startupPhase = setPhase(PhaseType.STARTUP);
                l_startupPhase.StartPhase(p_gameController, p_players, p_commandPromptView, p_commandParts, p_gameMap);
            }
        }
    }
    
    /**
     * Gets the next phase type in the game flow.
     * 
     * @return the startup phase as the next phase
     */
    @Override
    public PhaseType getNextPhase() {
        return PhaseType.STARTUP;
    }
    
    /**
     * Checks if a command is valid for the Map Editor phase.
     * 
     * @param p_command the command to validate
     * @return true if the command is valid for this phase, false otherwise
     */
    @Override
    public boolean validateCommand(String p_command) {
        return VALID_COMMANDS.contains(p_command.toLowerCase());
    }
}