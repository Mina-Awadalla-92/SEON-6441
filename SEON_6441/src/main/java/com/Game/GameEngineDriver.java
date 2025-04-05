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
        // Display welcome banner
        System.out.println("===================================================");
        System.out.println("   Welcome to Warzone - Risk Game Implementation   ");
        System.out.println("===================================================");
        System.out.println("Build 3: Advanced Features (Tournament Mode)");
        System.out.println();
        System.out.println("Follow the prompts to load a map, set up players,");
        System.out.println("and choose between Single Player or Tournament mode.");
        System.out.println("===================================================");
        System.out.println();
        
        // Create and start the game controller
        GameController l_gameController = new GameController();
        l_gameController.startGame();
    }
}