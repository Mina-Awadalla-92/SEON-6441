package com.Game.utils;

/**
 * Provides help documentation for the game commands.
 */
public class HelpDocumentation {
	
	/**
     * Gets detailed help documentation for Single Game Mode.
     * 
     * @return Single Game Mode help documentation
     */
    public static String getSingleGameHelp() {
        StringBuilder help = new StringBuilder();
        
        help.append("\n===== SINGLE GAME MODE HELP =====\n\n");
        help.append("Welcome to Warzone Single Game Mode!\n\n");
        
        help.append("Game Mode Overview:\n");
        help.append("- Play a strategic war game with various player types\n");
        help.append("- Choose a map and configure game parameters\n");
        help.append("- Select player strategies\n\n");
        
        help.append("Player Strategies:\n");
        help.append("1. Human Player\n");
        help.append("   - Requires your direct interaction\n");
        help.append("   - Make strategic decisions manually\n\n");
        
        help.append("2. Aggressive Player\n");
        help.append("   - Focuses on centralizing forces\n");
        help.append("   - Deploys armies on strongest countries\n");
        help.append("   - Always attacks with strongest country\n\n");
        
        help.append("3. Benevolent Player\n");
        help.append("   - Protects weak territories\n");
        help.append("   - Deploys armies on weakest countries\n");
        help.append("   - Avoids attacking\n\n");
        
        help.append("4. Random Player\n");
        help.append("   - Makes completely random decisions\n");
        help.append("   - Unpredictable strategy\n");
        help.append("   - Deploys, attacks, and moves armies randomly\n\n");
        
        help.append("5. Cheater Player\n");
        help.append("   - Uses unconventional tactics\n");
        help.append("   - Automatically conquers neighboring enemy territories\n");
        help.append("   - Doubles armies on countries near enemies\n\n");
        
        help.append("Game Configuration:\n");
        help.append("- Select a map file (*.map)\n");
        help.append("- Choose 2-5 players with different strategies\n");
        help.append("- Set maximum number of turns (10-50)\n\n");
        
        help.append("Automatic vs. Interactive Gameplay:\n");
        help.append("- If no human players are selected, the game runs automatically\n");
        help.append("- Watch computer players compete against each other\n");
        help.append("- Include human players for interactive gameplay\n\n");
        
        help.append("Winning the Game:\n");
        help.append("- Conquer all territories to win\n");
        help.append("- If no player conquers all territories within max turns, it's a draw\n\n");
        
        help.append("Tips:\n");
        help.append("- Experiment with different player strategy combinations\n");
        help.append("- Each strategy has unique strengths and weaknesses\n");
        help.append("- Observe how different strategies interact\n");
        
        return help.toString();
    }
    
    /**
     * Provides a concise help summary for Single Game Mode.
     * 
     * @return A brief overview of Single Game Mode
     */
    public static String getSingleGameQuickHelp() {
        return "\n===== SINGLE GAME MODE QUICK HELP =====\n" +
               "1. Choose a map file\n" +
               "2. Select 2-5 players (mix of human/computer)\n" +
               "3. Set max turns (10-50)\n" +
               "4. Start the game and conquer territories!\n";
    }
    
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