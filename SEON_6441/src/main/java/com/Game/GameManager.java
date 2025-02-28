package com.Game;

import com.Game.controller.CommandController;
import com.Game.model.game.GameState;
import com.Game.view.GameView;
import java.util.Scanner;

/**
 * Main facade class for the game application.
 */
public class GameManager {
    private GameState d_gameState;
    private GameView d_gameView;
    private CommandController d_commandController;
    private Scanner d_scanner;
    
    /**
     * Constructor initializing the game components.
     */
    public GameManager() {
        this.d_gameState = new GameState();
        this.d_gameView = new GameView();
        this.d_commandController = new CommandController(d_gameState, d_gameView);
        this.d_scanner = new Scanner(System.in);
    }
    
    /**
     * Starts the game and processes user commands.
     */
    public void startGame() {
        d_gameView.showWelcomeMessage();
        
        while (true) {
            d_gameView.showCommandPrompt();
            String l_command = d_scanner.nextLine().trim();
            d_commandController.processCommand(l_command);
        }
    }
}