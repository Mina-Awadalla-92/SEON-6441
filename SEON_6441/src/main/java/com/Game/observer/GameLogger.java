package com.Game.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GameLogger class serves as a facade for the logging system.
 * It initializes and manages the LogEntryBuffer and observers, providing a simplified
 * interface for the game components to log actions.
 */
public class GameLogger {
    
    /**
     * The log entry buffer that holds and distributes log entries.
     */
    private LogEntryBuffer d_logEntryBuffer;
    
    /**
     * File observer that writes logs to a file.
     */
    private FileLogObserver d_fileObserver;
    
    /**
     * Singleton instance of the GameLogger.
     */
    private static GameLogger d_instance;
    
    /**
     * Private constructor initializing the logger components.
     * 
     * @param p_logFilePath The path of the log file
     * @param p_appendToFile If true, append to existing file; if false, create new file
     */
    private GameLogger(String p_logFilePath, boolean p_appendToFile) {
        this.d_logEntryBuffer = new LogEntryBuffer();
        this.d_fileObserver = new FileLogObserver(p_logFilePath, p_appendToFile);
        
        // Register the file observer with the buffer
        this.d_logEntryBuffer.addObserver(d_fileObserver);
    }
    
    /**
     * Logs detailed information about the single game configuration.
     * 
     * @param mapFile The selected map file
     * @param maxTurns Maximum number of turns
     * @param playerStrategies List of player strategies
     */
    public void logSingleGameSetup(String mapFile, int maxTurns, java.util.List<String> playerStrategies) {
        StringBuilder setupLog = new StringBuilder();
        setupLog.append("===== SINGLE GAME CONFIGURATION =====\n");
        setupLog.append("Timestamp: ").append(getCurrentTimestamp()).append("\n");
        setupLog.append("Map: ").append(mapFile).append("\n");
        setupLog.append("Maximum Turns: ").append(maxTurns).append("\n");
        setupLog.append("Player Strategies:\n");
        
        for (int i = 0; i < playerStrategies.size(); i++) {
            setupLog.append("  Player ").append(i+1).append(": ")
                    .append(playerStrategies.get(i)).append("\n");
        }
        setupLog.append("======================================\n");
        
        logAction(setupLog.toString());
    }
    
    /**
     * Logs the result of a single game mode.
     * 
     * @param winner The name of the winning player or "Draw"
     * @param mapFile The map used for the game
     * @param turnsTaken Number of turns taken to complete the game
     */
    public void logSingleGameResult(String winner, String mapFile, int turnsTaken) {
        StringBuilder resultLog = new StringBuilder();
        resultLog.append("===== SINGLE GAME RESULT =====\n");
        resultLog.append("Timestamp: ").append(getCurrentTimestamp()).append("\n");
        resultLog.append("Map: ").append(mapFile).append("\n");
        resultLog.append("Turns Taken: ").append(turnsTaken).append("\n");
        
        if (winner.equals("Draw")) {
            resultLog.append("Result: DRAW\n");
            resultLog.append("No player managed to conquer all territories within the turn limit.\n");
        } else {
            resultLog.append("Winner: ").append(winner).append("\n");
            resultLog.append("Player successfully conquered all territories.\n");
        }
        resultLog.append("======================================\n");
        
        logAction(resultLog.toString());
    }
    
    /**
     * Logs detailed strategy information for each player type.
     */
    public void logPlayerStrategyInfo() {
        StringBuilder strategyLog = new StringBuilder();
        strategyLog.append("===== PLAYER STRATEGY DETAILS =====\n");
        strategyLog.append("Timestamp: ").append(getCurrentTimestamp()).append("\n");
        
        strategyLog.append("1. Human Player:\n");
        strategyLog.append("   - Requires user interaction to make decisions\n");
        
        strategyLog.append("2. Aggressive Player:\n");
        strategyLog.append("   - Focuses on centralization of forces\n");
        strategyLog.append("   - Deploys on strongest country\n");
        strategyLog.append("   - Always attacks with strongest country\n");
        strategyLog.append("   - Moves armies to maximize force aggregation\n");
        
        strategyLog.append("3. Benevolent Player:\n");
        strategyLog.append("   - Focuses on protecting weak countries\n");
        strategyLog.append("   - Deploys on weakest country\n");
        strategyLog.append("   - Never attacks\n");
        strategyLog.append("   - Moves armies to reinforce weaker countries\n");
        
        strategyLog.append("4. Random Player:\n");
        strategyLog.append("   - Makes completely random decisions\n");
        strategyLog.append("   - Deploys on random country\n");
        strategyLog.append("   - Attacks random neighboring countries\n");
        strategyLog.append("   - Moves armies randomly\n");
        
        strategyLog.append("5. Cheater Player:\n");
        strategyLog.append("   - Automatically conquers immediate neighboring enemy countries\n");
        strategyLog.append("   - Doubles the number of armies on countries with enemy neighbors\n");
        strategyLog.append("======================================\n");
        
        logAction(strategyLog.toString());
    }
    
    /**
     * Gets the current timestamp formatted as a string.
     * 
     * @return Formatted current timestamp
     */
    private String getCurrentTimestamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * Gets the singleton instance of GameLogger.
     * Creates a new instance if one doesn't exist.
     * 
     * @param p_logFilePath The path of the log file
     * @param p_appendToFile If true, append to existing file; if false, create new file
     * @return The GameLogger singleton instance
     */
    public static GameLogger getInstance(String p_logFilePath, boolean p_appendToFile) {
        if (d_instance == null) {
            d_instance = new GameLogger(p_logFilePath, p_appendToFile);
        }
        return d_instance;
    }
    
    /**
     * Gets the singleton instance with default configuration if it exists.
     * 
     * @return The existing GameLogger instance or null if not initialized
     */
    public static GameLogger getInstance() {
        return d_instance;
    }
    
    /**
     * Logs a game action.
     * 
     * @param p_logEntry The log entry describing the action
     */
    public void logAction(String p_logEntry) {
        this.d_logEntryBuffer.logAction(p_logEntry);
    }
    
    /**
     * Logs a phase change in the game.
     * 
     * @param p_phaseName The name of the new phase
     */
    public void logPhaseChange(String p_phaseName) {
        this.d_logEntryBuffer.logPhaseChange(p_phaseName);
    }
    
    /**
     * Gets the full log buffer content.
     * 
     * @return The log buffer as a string
     */
    public String getLogBuffer() {
        return this.d_logEntryBuffer.getLogBuffer();
    }
    
    /**
     * Clears the log buffer.
     */
    public void clearLogBuffer() {
        this.d_logEntryBuffer.clearLogBuffer();
    }
    
    /**
     * Gets the file path of the log file.
     * 
     * @return The log file path
     */
    public String getLogFilePath() {
        return this.d_fileObserver.getLogFilePath();
    }
    
    /**
     * Sets the log file path.
     * 
     * @param p_logFilePath The new log file path
     */
    public void setLogFilePath(String p_logFilePath) {
        this.d_fileObserver.setLogFilePath(p_logFilePath);
    }
}