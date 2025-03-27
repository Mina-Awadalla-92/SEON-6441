package com.Game.Phases;

import com.Game.model.Player;
import com.Game.controller.GameController;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import com.Game.observer.GameLogger;

import java.util.List;

/**
 * Abstract class representing a game phase in the State pattern.
 * Different phases of the game will implement this class to provide phase-specific behavior.
 * This serves as the State class in the State pattern.
 */
public abstract class Phase {

    /**
     * Reference to the game logger.
     */
    protected GameLogger d_gameLogger;
    
    /**
     * Constructor to initialize the phase with logging.
     */
    public Phase() {
        this.d_gameLogger = GameLogger.getInstance();
    }

    /**
     * Starts the current phase with the provided game controller, players, and command prompt view.
     *
     * @param p_gameController     the game controller handling the game state.
     * @param p_players            list of players participating in the game.
     * @param p_commandPromptView  view handling user interactions through the command prompt.
     * @param p_commandParts       the command parts passed as input.
     * @param p_gameMap            The game map containing all territories and their connections.
     */
    public abstract void StartPhase(GameController p_gameController, List<Player> p_players, CommandPromptView p_commandPromptView, String[] p_commandParts, Map p_gameMap);

    /**
     * Transitions to the next game phase based on the specified phase type.
     * In the State pattern, this is the state transition method.
     *
     * @param p_nextPhaseType the type of the next phase to transition to
     * @return the new Phase instance corresponding to the given phase type
     * @throws IllegalArgumentException if an invalid phase type is provided
     */
    public Phase setPhase(PhaseType p_nextPhaseType) {
        if (d_gameLogger != null) {
            d_gameLogger.logPhaseChange(p_nextPhaseType.name());
        }
        
        switch (p_nextPhaseType) {
            case ISSUE_ORDER:
                System.out.println("Game phase changed to: IssueOrderPhase");
                return new IssueOrderPhase();
            case ORDER_EXECUTION:
                System.out.println("Game phase changed to: OrderExecutionPhase");
                return new OrderExecutionPhase();
            case STARTUP:
                System.out.println("Game phase changed to: StartupPhase");
                return new StartupPhase();
            case MAP_EDITOR:
                System.out.println("Game phase changed to: MapEditorPhase");
                return new MapEditorPhase();
            default:
                throw new IllegalArgumentException("Invalid Phase Type!");
        }
    }
    
    /**
     * Gets the next phase type based on the current phase.
     * This is used to determine the natural phase progression.
     * 
     * @return the next phase type in the game flow
     */
    public abstract PhaseType getNextPhase();
    
    /**
     * Checks if a command is valid for the current phase.
     * 
     * @param p_command the command to validate
     * @return true if the command is valid for this phase, false otherwise
     */
    public abstract boolean validateCommand(String p_command);
}