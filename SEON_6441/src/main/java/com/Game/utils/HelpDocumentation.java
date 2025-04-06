package com.Game.utils;

/**
 * Provides help documentation for the game commands.
 */
public class HelpDocumentation {
    
    /**
     * Gets the documentation for the tournament command.
     * 
     * @return Tournament command documentation
     */
    public static String getTournamentHelp() {
        StringBuilder help = new StringBuilder();
        
        help.append("\n===== TOURNAMENT MODE HELP =====\n\n");
        help.append("The tournament command allows you to automate the playing of multiple games\n");
        help.append("across different maps with various computer player strategies.\n\n");
        
        help.append("Command Format:\n");
        help.append("tournament -M <list_of_maps> -P <list_of_player_strategies> -G <num_of_games> -D <max_turns>\n\n");
        
        help.append("Parameters:\n");
        help.append("  -M: List of map files (1-5 maps)\n");
        help.append("  -P: List of player strategies (2-4 strategies)\n");
        help.append("      Available strategies: aggressive, benevolent, random, cheater\n");
        help.append("  -G: Number of games to play on each map (1-5 games)\n");
        help.append("  -D: Maximum number of turns for each game (10-50 turns)\n\n");
        
        help.append("Example:\n");
        help.append("tournament -M canada.map swiss.map -P aggressive benevolent random cheater -G 3 -D 20\n\n");
        
        help.append("This will play 3 games on each map (canada.map and swiss.map) with 4 players,\n");
        help.append("each using a different strategy. Each game ends when a player wins or after 20 turns.\n");
        
        return help.toString();
    }
    
    /**
     * Gets the documentation for the main menu.
     * 
     * @return Main menu documentation
     */
    public static String getMainMenuHelp() {
        StringBuilder help = new StringBuilder();
        
        help.append("\n===== MAIN MENU HELP =====\n\n");
        help.append("Welcome to Warzone - Risk Game Implementation\n\n");
        
        help.append("Game Modes:\n");
        help.append("1. Single Player Mode - Play a standard game with human and computer players\n");
        help.append("2. Tournament Mode - Run automated tournaments between computer players\n\n");
        
        help.append("To get started:\n");
        help.append("1. Load a map using 'loadmap' command\n");
        help.append("2. Select a game mode\n");
        help.append("3. Follow the mode-specific instructions\n\n");
        
        help.append("For more information on a specific command, type 'help <command>'\n");
        
        return help.toString();
    }
}