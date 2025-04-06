package com.Game.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.Game.utils.HelpDocumentation;
import com.Game.utils.SingleGameUtil;

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
     * Interactive method to configure single game mode settings.
     * 
     * @return List containing map file, max turns, and player strategies
     */
    public List<Object> configureSingleGameSettings() {
        List<Object> gameSettings = new ArrayList<>();
        
        // Display help information
        System.out.println(HelpDocumentation.getSingleGameQuickHelp());
        
        // Map file selection
        String mapFile = selectMapFile();
        gameSettings.add(mapFile);
        
        // Maximum turns configuration
        int maxTurns = configureMaxTurns();
        gameSettings.add(maxTurns);
        
        // Player strategies configuration
        List<String> playerStrategies = configurePlayerStrategies();
        gameSettings.add(playerStrategies);
        
        return gameSettings;
    }

    /**
     * Interactively select a map file.
     * 
     * @return Selected map file name
     */
    private String selectMapFile() {
        while (true) {
            System.out.println("\n===== MAP SELECTION =====");
            System.out.println("Available maps in resources:");
            System.out.println("1. canada.map");
            System.out.println("2. swiss.map");
            System.out.println("3. MiddleEast-Qatar.map");
            System.out.println("4. Enter custom map file");
            
            int choice = getInteger("Select map (1-4)");
            
            switch (choice) {
                case 1:
                    return "canada.map";
                case 2:
                    return "swiss.map";
                case 3:
                    return "MiddleEast-Qatar.map";
                case 4:
                    return getString("Enter full path to map file");
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Configures the maximum number of turns for the game.
     * 
     * @return Number of maximum turns
     */
    private int configureMaxTurns() {
        while (true) {
            System.out.println("\n===== TURN CONFIGURATION =====");
            int maxTurns = getInteger("Enter maximum number of turns (10-50)");
            
            if (maxTurns >= 10 && maxTurns <= 50) {
                return maxTurns;
            }
            
            System.out.println("Invalid input. Maximum turns must be between 10 and 50.");
        }
    }

    /**
     * Interactively configure player strategies.
     * 
     * @return List of selected player strategies
     */
    private List<String> configurePlayerStrategies() {
        List<String> strategies = new ArrayList<>();
        
        while (true) {
            System.out.println("\n===== PLAYER STRATEGY CONFIGURATION =====");
            System.out.println("Current Players: " + strategies.size());
            
            // Display available strategies
            System.out.println("\nAvailable Strategies:");
            System.out.println("1. Human Player");
            System.out.println("2. Aggressive Computer Player");
            System.out.println("3. Benevolent Computer Player");
            System.out.println("4. Random Computer Player");
            System.out.println("5. Cheater Computer Player");
            System.out.println("6. Finish Player Setup");
            
            // Show strategy recommendations if applicable
            List<String> recommendations = SingleGameUtil.getStrategyRecommendations(strategies);
            if (!recommendations.isEmpty()) {
                System.out.println("\nRecommended Strategies: " + 
                    recommendations.stream()
                        .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                        .collect(Collectors.joining(", ")));
            }
            
            int choice = getInteger("Enter your choice");
            
            switch (choice) {
                case 1:
                    strategies.add("human");
                    break;
                case 2:
                    strategies.add("aggressive");
                    break;
                case 3:
                    strategies.add("benevolent");
                    break;
                case 4:
                    strategies.add("random");
                    break;
                case 5:
                    strategies.add("cheater");
                    break;
                case 6:
                    // Validate player setup
                    if (strategies.size() < 2) {
                        System.out.println("Error: You must have at least 2 players.");
                        continue;
                    }
                    
                    // Confirm strategy selection
                    System.out.println("\nSelected Strategies:");
                    for (int i = 0; i < strategies.size(); i++) {
                        System.out.println("Player " + (i+1) + ": " + 
                            strategies.get(i).substring(0, 1).toUpperCase() + 
                            strategies.get(i).substring(1));
                    }
                    
                    boolean confirm = getConfirmation("Confirm these player strategies?");
                    if (confirm) {
                        return strategies;
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Provides help information about player strategies.
     */
    public void showPlayerStrategyHelp() {
        System.out.println(HelpDocumentation.getSingleGameHelp());
        getConfirmation("Press Enter to continue");
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
     * Provides an interactive player setup for single game mode.
     * Allows adding human and computer players with different strategies.
     * 
     * @return List of player strategies selected by the user
     */
    public List<String> configureSingleGamePlayers() {
        List<String> playerStrategies = new ArrayList<>();
        
        while (true) {
            System.out.println("\n===== Single Game Player Setup =====");
            System.out.println("Current Players: " + playerStrategies.size());
            System.out.println("\nChoose a player type to add:");
            System.out.println("1. Human Player");
            System.out.println("2. Aggressive Computer Player");
            System.out.println("3. Benevolent Computer Player");
            System.out.println("4. Random Computer Player");
            System.out.println("5. Cheater Computer Player");
            System.out.println("6. Finish Player Setup");
            
            int choice = getInteger("Enter your choice");
            
            switch (choice) {
                case 1:
                    playerStrategies.add("human");
                    break;
                case 2:
                    playerStrategies.add("aggressive");
                    break;
                case 3:
                    playerStrategies.add("benevolent");
                    break;
                case 4:
                    playerStrategies.add("random");
                    break;
                case 5:
                    playerStrategies.add("cheater");
                    break;
                case 6:
                    // Validate player setup
                    if (playerStrategies.size() < 2) {
                        System.out.println("Error: You must have at least 2 players.");
                        continue;
                    }
                    return playerStrategies;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    /**
     * Configures the maximum number of turns for the game.
     * 
     * @return The number of maximum turns
     */
    private int getMaxTurnsConfiguration() {
        while (true) {
            int maxTurns = getInteger("Enter maximum number of turns (10-50)");
            
            if (maxTurns >= 10 && maxTurns <= 50) {
                return maxTurns;
            }
            
            System.out.println("Invalid input. Maximum turns must be between 10 and 50.");
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