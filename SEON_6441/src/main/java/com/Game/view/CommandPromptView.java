package com.Game.view;

import java.util.Scanner;

/**
 * Handles user input through the command line.
 * This class is responsible for gathering commands and input from the user.
 */
public class CommandPromptView {
    
    /**
     * Scanner object used to read user input.
     */
    private Scanner d_scanner;
    
    /**
     * Default constructor initializes the scanner.
     */
    public CommandPromptView() {
        d_scanner = new Scanner(System.in);
    }
    
    /**
     * Prompts the user to enter a command and returns the input.
     * 
     * @return The user's command input as a string
     */
    public String getCommand() {
        System.out.print("Enter command: ");
        return d_scanner.nextLine().trim();
    }
    
    /**
     * Gets the next deploy order from a player or checks if they want to finish.
     * 
     * @param p_playerName The name of the player issuing orders
     * @param p_remainingArmies The number of remaining armies the player can deploy
     * @return The player's order as a string
     */
    public String getPlayerOrder(String p_playerName, int p_remainingArmies) {
        System.out.print("Hi " + p_playerName + ", please enter your next deploy order, or type FINISH: ");
        return d_scanner.nextLine().trim();
    }
    
    /**
     * Gets confirmation from the user.
     * 
     * @param p_message The message to display when requesting confirmation
     * @return true if the user confirms, false otherwise
     */
    public boolean getConfirmation(String p_message) {
        System.out.print(p_message + " (y/n): ");
        String l_response = d_scanner.nextLine().trim().toLowerCase();
        return l_response.equals("y") || l_response.equals("yes");
    }
    
    /**
     * Gets a filename from the user.
     * 
     * @param p_prompt The prompt to display when requesting a filename
     * @return The filename entered by the user
     */
    public String getFilename(String p_prompt) {
        System.out.print(p_prompt + ": ");
        return d_scanner.nextLine().trim();
    }
    
    /**
     * Gets an integer value from the user.
     * 
     * @param p_prompt The prompt to display when requesting an integer
     * @return The integer entered by the user, or -1 if input is invalid
     */
    public int getInteger(String p_prompt) {
        System.out.print(p_prompt + ": ");
        try {
            return Integer.parseInt(d_scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Gets a string value from the user.
     * 
     * @param p_prompt The prompt to display when requesting a string
     * @return The string entered by the user
     */
    public String getString(String p_prompt) {
        System.out.print(p_prompt + ": ");
        return d_scanner.nextLine().trim();
    }
    
    /**
     * Closes the scanner to prevent resource leaks.
     * This should be called when the application is terminating.
     */
    public void closeScanner() {
        d_scanner.close();
    }
}