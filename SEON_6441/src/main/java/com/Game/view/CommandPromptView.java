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
     * Gets a game mode selection from the user.
     * 
     * @return The selected mode (1 for single player, 2 for tournament)
     */
    public int getGameModeSelection() {
        System.out.print("Select game mode (1 for Single Player, 2 for Tournament): ");
        try {
            return Integer.parseInt(d_scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Invalid selection
        }
    }

    /**
     * Gets a tournament command from the user with guided input.
     * This breaks down the tournament command into parts for easier input.
     * 
     * @return The complete tournament command as a string
     */
    public String getTournamentCommand() {
        StringBuilder command = new StringBuilder("tournament");
        
        // Get map files
        System.out.println("\nEnter map files (1-5 maps, space separated):");
        String mapFiles = d_scanner.nextLine().trim();
        command.append(" -M ").append(mapFiles);
        
        // Get player strategies
        System.out.println("\nEnter player strategies (2-4 strategies, space separated):");
        System.out.println("Available strategies: aggressive, benevolent, random, cheater");
        String playerStrategies = d_scanner.nextLine().trim();
        command.append(" -P ").append(playerStrategies);
        
        // Get number of games
        System.out.println("\nEnter number of games per map (1-5):");
        String numberOfGames = d_scanner.nextLine().trim();
        command.append(" -G ").append(numberOfGames);
        
        // Get max turns
        System.out.println("\nEnter maximum number of turns per game (10-50):");
        String maxTurns = d_scanner.nextLine().trim();
        command.append(" -D ").append(maxTurns);
        
        return command.toString();
    }

    /**
     * Gets a yes/no response for a simplified tournament setup.
     * 
     * @param message The message to display
     * @return true for yes, false for no
     */
    public boolean getYesNoResponse(String message) {
        System.out.print(message + " (y/n): ");
        String response = d_scanner.nextLine().trim().toLowerCase();
        return response.startsWith("y");
    }

    /**
     * Gets a tournament setup with default settings or custom options.
     * 
     * @return The tournament command as a string
     */
    public String getQuickTournamentSetup() {
        System.out.println("\n=== Tournament Setup ===");
        
        boolean useDefaults = getYesNoResponse("Use default tournament settings?");
        
        if (useDefaults) {
            return "tournament -M canada.map -P aggressive benevolent random cheater -G 2 -D 20";
        } else {
            return getTournamentCommand();
        }
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