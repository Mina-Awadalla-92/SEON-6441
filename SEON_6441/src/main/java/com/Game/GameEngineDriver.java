package com.Game;

import com.Game.controller.GameController;

/**
 * The entry point for the Warzone game application.
 * This class initializes the GameController and starts the game.
 */
public class GameEngineDriver {

    /**
     * The main method that starts the game.
     * 
     * @param p_args Command-line arguments (not used)
     */
    public static void main(String[] p_args) {
        GameController l_gameController = new GameController();
        l_gameController.startGame();
    }
}