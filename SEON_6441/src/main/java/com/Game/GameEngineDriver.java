package com.Game;

/**
 * Main entry point for the game application.
 */
public class GameEngineDriver {
    /**
     * Main method to start the application.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        GameManager l_gameManager = new GameManager();
        l_gameManager.startGame();
    }
}