package com.Game.Phases;

import com.Game.controller.GameController;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;

import java.util.List;

/**
 * Represents the Startup phase of the game.
 * This phase is responsible for initializing game settings, assigning players, and preparing the game state before gameplay begins.
 */
public class StartupPhase extends Phase {

    /**
     * Starts the Startup phase with the provided game controller, players, and command prompt view.
     * This method will handle game setup, such as initializing players and allocating resources.
     *
     * @param p_gameController    the game controller handling the game state
     * @param d_players           list of players participating in the game
     * @param l_commandPromptView view handling user interactions through the command prompt
     */
    @Override
    public void StartPhase(GameController p_gameController, List<Player> d_players, CommandPromptView l_commandPromptView) {
        // Implementation for the Startup phase will be added here.
    }
}
