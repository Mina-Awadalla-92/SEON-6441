package com.Game.controller;

import com.Game.model.game.GameState;
import com.Game.view.GameView;

/**
 * Main controller for processing game commands.
 */
public class CommandController {
    private GameState d_gameState;
    private GameView d_gameView;
    private MapEditorController d_mapEditorController;
    private GameSetupController d_gameSetupController;
    private GamePlayController d_gamePlayController;
    
    /**
     * Constructor initializing the controller with needed dependencies.
     * 
     * @param p_gameState The game state.
     * @param p_gameView The game view.
     */
    public CommandController(GameState p_gameState, GameView p_gameView) {
        this.d_gameState = p_gameState;
        this.d_gameView = p_gameView;
        
        // Initialize phase-specific controllers
        this.d_mapEditorController = new MapEditorController(p_gameState, p_gameView);
        this.d_gameSetupController = new GameSetupController(p_gameState, p_gameView);
        this.d_gamePlayController = new GamePlayController(p_gameState, p_gameView);
    }
    
    /**
     * Processes a command based on the current game phase.
     * 
     * @param p_command The command to process.
     */
    public void processCommand(String p_command) {
        String[] l_commandParts = p_command.split("\\s+");
        
        if (l_commandParts.length == 0) {
            return;
        }
        
        String l_command = l_commandParts[0];
        
        if (l_command.equals("exit")) {
            d_gameView.showMessage("Exiting the program.");
            System.exit(0); // Exit the program
        } 
        
        // Route command to appropriate controller based on phase
        switch (d_gameState.getCurrentPhase()) {
            case GameState.MAP_EDITING_PHASE:
                d_mapEditorController.handleCommand(l_commandParts);
                break;
            case GameState.STARTUP_PHASE:
                d_gameSetupController.handleCommand(l_commandParts);
                break;
            case GameState.MAIN_GAME_PHASE:
                d_gamePlayController.handleCommand(l_commandParts);
                break;
        }
    }
}