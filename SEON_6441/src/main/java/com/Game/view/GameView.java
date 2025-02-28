package com.Game.view;

/**
 * Responsible for displaying game-related information to the user.
 */
public class GameView {
    
    /**
     * Displays a welcome message when the game starts.
     */
    public void showWelcomeMessage() {
        System.out.println("Welcome to Warzone Game!");
    }
    
    /**
     * Displays the command prompt.
     */
    public void showCommandPrompt() {
        System.out.print("Enter command: ");
    }
    
    /**
     * Displays a message to the user.
     * 
     * @param p_message The message to display.
     */
    public void showMessage(String p_message) {
        System.out.println(p_message);
    }
    
    /**
     * Displays the current map.
     * 
     * @param p_mapString String representation of the map.
     */
    public void showMap(String p_mapString) {
        System.out.println("****Map*****");
        System.out.println(p_mapString);
    }
}