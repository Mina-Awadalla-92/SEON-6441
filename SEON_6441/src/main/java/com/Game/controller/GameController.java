package com.Game.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.Game.Phases.IssueOrderPhase;
import com.Game.Phases.MapEditorPhase;
import com.Game.Phases.OrderExecutionPhase;
import com.Game.Phases.Phase;
import com.Game.Phases.PhaseType;
import com.Game.Phases.StartupPhase;
import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.model.HumanPlayer;
import com.Game.observer.GameLogger;
import com.Game.utils.MapLoader;
import com.Game.view.CommandPromptView;
import com.Game.view.GameView;

/**
 * Main controller class that coordinates the game flow. This class manages the
 * game state, delegates to specialized controllers, and handles transitions
 * between different game phases. In the State pattern, this class serves as the
 * Context.
 */
public class GameController {

	/**
	 * Constant representing the Map Editing phase of the game.
	 */
	public static final int MAP_EDITING_PHASE = 0;

	/**
	 * Constant representing the Startup phase of the game.
	 */
	public static final int STARTUP_PHASE = 1;

	/**
	 * Constant representing the Main Game phase of the game.
	 */
	public static final int MAIN_GAME_PHASE = 2;

	/**
	 * The current game map being used.
	 */
	private Map d_gameMap;

	/**
	 * The map loader responsible for loading and validating maps.
	 */
	private MapLoader d_mapLoader;

	/**
	 * The file path of the current map.
	 */
	private String d_mapFilePath;

	/**
	 * The list of players in the game.
	 */
	private List<Player> d_players;

	/**
	 * The neutral player used for certain game mechanics.
	 */
	private HumanPlayer d_neutralPlayer;

	/**
	 * Indicates whether the game has started or not.
	 */
	private boolean d_gameStarted;

	/**
	 * Represents the current phase of the game.
	 */
	private int d_currentPhase;

	/**
	 * Indicates whether the countries have been assigned to players.
	 */
	private boolean d_countriesAssigned;

	/**
	 * The view for displaying game information.
	 */
	private GameView d_view;

	/**
	 * The view for handling command input.
	 */
	private CommandPromptView d_commandPromptView;

	/**
	 * Controller for map editing operations.
	 */
	private MapEditorController d_mapEditorController;

	/**
	 * Controller for gameplay operations.
	 */
	private GamePlayController d_gamePlayController;

	/**
	 * Represents the startup phase of the game.
	 */
	private Phase d_startupPhase;

	/**
	 * Game logger for logging game events.
	 */
	private GameLogger d_gameLogger;
	/**
	 * The current phase state in the State pattern.
	 */
	private Phase d_currentState;

	/**
	 * Default constructor that initializes the game controller.
	 */
	public GameController() {
		this.d_gameMap = new Map();
		this.d_mapLoader = new MapLoader();
		this.d_players = new ArrayList<>();
		this.d_neutralPlayer = new HumanPlayer("Neutral");
		this.d_gameStarted = false;
		this.d_currentPhase = MAP_EDITING_PHASE;
		this.d_countriesAssigned = false;

		this.d_view = new GameView();
		this.d_commandPromptView = new CommandPromptView();
		this.d_mapEditorController = new MapEditorController(this, d_gameMap, d_mapLoader);
		this.d_gamePlayController = new GamePlayController(this, d_gameMap, d_players);

		// Create logs directory if it doesn't exist
		File logsDir = new File("logs");
		if (!logsDir.exists()) {
			logsDir.mkdir();
		}

		// Initialize the game logger with a timestamped log file name
		SimpleDateFormat l_dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String l_timestamp = l_dateFormat.format(new Date());
		String l_logFilePath = "logs/warzone_game_" + l_timestamp + ".log";
		this.d_gameLogger = GameLogger.getInstance(l_logFilePath, false);

		// Initialize with the MapEditor state
		this.d_currentState = new MapEditorPhase();
	}

	/**
	 * Sets the current phase of the game. This implements the context role in the
	 * State pattern.
	 *
	 * @param p_phaseType The phase type to set
	 */
	// Add a debug flag to track phase changes
	private boolean isProcessingPhaseChange = false;

	/**
	 * Sets the current phase of the game to the specified {@link PhaseType}.
	 *
	 * This method manages the game phase transitions, ensuring that phase changes
	 * are not nested (to prevent recursion). It logs the action of changing phases
	 * if a logger is provided, and updates the game state accordingly. The method
	 * handles four predefined phases: {@link PhaseType#MAP_EDITOR},
	 * {@link PhaseType#STARTUP}, {@link PhaseType#ISSUE_ORDER}, and
	 * {@link PhaseType#ORDER_EXECUTION}. If an invalid phase is provided, an
	 * {@link IllegalArgumentException} is thrown.
	 *
	 * @param p_phaseType The {@link PhaseType} representing the new phase to set.
	 *
	 * @throws IllegalArgumentException If the provided {@code p_phaseType} is not
	 *                                  valid.
	 */
	public void setPhase(PhaseType p_phaseType) {
		if (isProcessingPhaseChange) {
			System.out.println("WARNING: Nested phase change detected. Skipping to prevent duplication.");
			return; // Prevent recursive phase changes
		}

		isProcessingPhaseChange = true; // Set flag to indicate we're processing a phase change

		try {
			if (d_gameLogger != null) {
				d_gameLogger.logAction("Changing phase to: " + p_phaseType);
			}

			switch (p_phaseType) {
			case MAP_EDITOR:
				this.d_currentState = new MapEditorPhase();
				d_currentPhase = MAP_EDITING_PHASE;
				break;
			case STARTUP:
				this.d_currentState = new StartupPhase();
				d_currentPhase = STARTUP_PHASE;
				break;
			case ISSUE_ORDER:
				this.d_currentState = new IssueOrderPhase();
				d_currentPhase = MAIN_GAME_PHASE;
				break;
			case ORDER_EXECUTION:
				this.d_currentState = new OrderExecutionPhase();
				d_currentPhase = MAIN_GAME_PHASE;
				break;
			default:
				throw new IllegalArgumentException("Invalid Phase Type: " + p_phaseType);
			}

			System.out.println("Game phase changed to: " + p_phaseType);
		} finally {
			isProcessingPhaseChange = false; // Reset flag when done
		}
	}

	/**
	 * Gets the current phase state.
	 *
	 * @return The current phase state
	 */
	public Phase getCurrentState() {
		return d_currentState;
	}

	/**
	 * Starts the game and processes user commands. Provides a command prompt that's
	 * available throughout the game. This method has been updated to use the State
	 * pattern.
	 */
	public void startGame() {
		// Initialize with MapEditor phase
		setPhase(PhaseType.MAP_EDITOR);

		if (d_gameLogger != null) {
			d_gameLogger.logAction("Game started");
		}

		d_view.displayWelcomeMessage();
		boolean l_isMapLoaded = false;

		while (true) {
			String l_input;
			String[] l_commandParts;

			if (d_currentPhase == MAP_EDITING_PHASE) {
				d_view.displayMapEditingMenu();
				l_input = d_commandPromptView.getCommand();
				l_commandParts = l_input.split("\\s+");

				if (l_commandParts.length == 0) {
					continue;
				}

				String l_command = l_commandParts[0];
				if (d_gameLogger != null) {
					d_gameLogger.logAction("Command entered: " + l_input);
				}

				if (l_command.equals("exit")) {
					d_view.displayMessage("Exiting the program.");
					if (d_gameLogger != null) {
						d_gameLogger.logAction("Program exited");
					}
					d_commandPromptView.closeScanner();
					System.exit(0);
				}

				// Validate command for current phase
				if (!getCurrentState().validateCommand(l_command)) {
					d_view.displayError("Command '" + l_command + "' is not valid in the current phase.");
					if (d_gameLogger != null) {
						d_gameLogger.logAction("Invalid command '" + l_command + "' for current phase");
					}
					continue;
				}

				if (!l_isMapLoaded && !l_command.equals("editmap") && !l_command.equals("loadmap")) {
					d_view.displayError("You must load/edit a map first using the 'editmap' or 'loadmap' command.");
					if (d_gameLogger != null) {
						d_gameLogger.logAction("Error: Attempt to use command without loading a map first");
					}
				} else {
					l_isMapLoaded = d_mapEditorController.handleCommand(l_commandParts, l_command, l_isMapLoaded);

					// Let the current state handle any phase-specific logic
					getCurrentState().StartPhase(this, d_players, d_commandPromptView, l_commandParts, d_gameMap);
				}
			} // In the STARTUP_PHASE section of the loop in startGame method
			else if (d_currentPhase == STARTUP_PHASE) {
				d_view.displayStartupMenu();
				l_input = d_commandPromptView.getCommand();
				l_commandParts = l_input.split("\\s+");

				if (l_commandParts.length == 0) {
					continue;
				}

				String l_command = l_commandParts[0];
				if (d_gameLogger != null) {
					d_gameLogger.logAction("Command entered: " + l_input);
				}

				if (l_command.equals("exit")) {
					d_view.displayMessage("Exiting the program.");
					if (d_gameLogger != null) {
						d_gameLogger.logAction("Program exited");
					}
					d_commandPromptView.closeScanner();
					System.exit(0);
				}

				// Validate command for current phase
				if (!getCurrentState().validateCommand(l_command)) {
					d_view.displayError("Command '" + l_command + "' is not valid in the current phase.");
					if (d_gameLogger != null) {
						d_gameLogger.logAction("Invalid command '" + l_command + "' for current phase");
					}
					continue;
				}

				handleStartupCommand(l_commandParts, l_command);

				// Skip the phase's StartPhase method which might be causing the duplication
				// getCurrentState().StartPhase(this, d_players, d_commandPromptView,
				// l_commandParts, d_gameMap);
			} else if (d_currentPhase == MAIN_GAME_PHASE) {
				d_view.displayMainGameMenu();
				l_input = d_commandPromptView.getCommand();
				l_commandParts = l_input.split("\\s+");

				if (l_commandParts.length == 0) {
					continue;
				}

				String l_command = l_commandParts[0];
				if (d_gameLogger != null) {
					d_gameLogger.logAction("Command entered: " + l_input);
				}

				if (l_command.equals("exit")) {
					d_view.displayMessage("Exiting the program.");
					if (d_gameLogger != null) {
						d_gameLogger.logAction("Program exited");
					}
					d_commandPromptView.closeScanner();
					System.exit(0);
				}

				// SKIP VALIDATION TEMPORARILY
				// if (!getCurrentState().validateCommand(l_command)) {
				// d_view.displayError("Command '" + l_command + "' is not valid in the current
				// phase.");
				// if (d_gameLogger != null) {
				// d_gameLogger.logAction("Invalid command '" + l_command + "' for current
				// phase");
				// }
				// continue;
				// }

				d_gamePlayController.handleCommand(l_commandParts, l_command);
			}
		}
	}

	/**
	 * Handles commands specific to the startup phase.
	 *
	 * @param p_commandParts Array of command components
	 * @param p_command      The main command
	 */
	private void handleStartupCommand(String[] p_commandParts, String p_command) {
		switch (p_command) {
		case "showmap":
			d_view.displayMap(d_gameMap, d_players);
			d_gameLogger.logAction("Map displayed");
			break;
		case "gameplayer":
			if (p_commandParts.length >= 3) {
				String l_action = p_commandParts[1];
				String l_playerName = p_commandParts[2];
				handleGamePlayer(l_action, l_playerName);
			} else {
				d_view.displayError("Usage: gameplayer -add playerName OR gameplayer -remove playerName");
				d_gameLogger.logAction("Error: Invalid gameplayer command format");
			}
			break;
		case "assigncountries":
			// Add map validation before assigning countries
			if (d_mapLoader.validateMap()) {
				if (d_gamePlayController.handleAssignCountries()) {
					d_countriesAssigned = true;
					if (d_gameLogger != null) {
						d_gameLogger.logAction("Countries assigned to players");
					}
				} else {
					if (d_gameLogger != null) {
						d_gameLogger.logAction("Failed to assign countries");
					}
				}
			} else {
				d_view.displayError("Map validation failed. Please validate the map before assigning countries.");
				if (d_gameLogger != null) {
					d_gameLogger.logAction("Error: Map validation failed before assigning countries");
				}
			}
			break;
		case "startgame":
			if (!d_countriesAssigned) {
				d_view.displayError(
						"You must assign countries first using 'assigncountries' before starting the game.");
				d_gameLogger.logAction("Error: Attempt to start game without assigning countries");
			} else {
				d_gamePlayController.startMainGame();
				d_gameLogger.logPhaseChange("MAIN GAME");
			}
			break;
		case "editmap":
			// Allow returning to map editing phase
			d_currentPhase = MAP_EDITING_PHASE;
			d_gameLogger.logPhaseChange("MAP EDITING");
			d_mapEditorController.handleCommand(p_commandParts, p_command, true);
			break;
		case "loadmap":
			// Allow loading a different map
			d_currentPhase = MAP_EDITING_PHASE;
			d_gameLogger.logPhaseChange("MAP EDITING");
			d_mapEditorController.handleCommand(p_commandParts, p_command, true);
			break;
		default:
			d_view.displayError("Unknown command or invalid for current phase: " + p_command);
			d_gameLogger.logAction("Error: Unknown command '" + p_command + "' in startup phase");
		}
	}

	/**
	 * Handles the gameplayer command to add or remove players.
	 *
	 * @param p_action     The action to perform (add/remove)
	 * @param p_playerName The name of the player to add/remove
	 */
	public void handleGamePlayer(String p_action, String p_playerName) {
		if (p_action.equals("-add")) {
			// Check if player already exists - case-insensitive to be robust
			boolean l_playerExists = false;
			for (Player l_player : d_players) {
				if (l_player.getName().equalsIgnoreCase(p_playerName)) {
					l_playerExists = true;
					break;
				}
			}

			if (!l_playerExists) {
				d_players.add(new HumanPlayer(p_playerName));
				d_view.displayMessage("Player added: " + p_playerName);
				if (d_gameLogger != null) {
					d_gameLogger.logAction("Player added: " + p_playerName);
				}
			} else {
				d_view.displayError("Player already exists: " + p_playerName);
				if (d_gameLogger != null) {
					d_gameLogger.logAction("Error: Player already exists: " + p_playerName);
				}
			}
		} else if (p_action.equals("-remove")) {
			d_players.removeIf(player -> player.getName().equals(p_playerName));
			d_view.displayMessage("Player removed: " + p_playerName);
			if (d_gameLogger != null) {
				d_gameLogger.logAction("Player removed: " + p_playerName);
			}
		} else {
			d_view.displayError("Invalid action for gameplayer.");
			if (d_gameLogger != null) {
				d_gameLogger.logAction("Error: Invalid action for gameplayer: " + p_action);
			}
		}
	}

	/**
	 * Gets the game view.
	 *
	 * @return The game view
	 */
	public GameView getView() {
		return d_view;
	}

	/**
	 * Gets the command prompt view.
	 *
	 * @return The command prompt view
	 */
	public CommandPromptView getCommandPromptView() {
		return d_commandPromptView;
	}

	/**
	 * Gets the game map.
	 *
	 * @return The game map
	 */
	public Map getGameMap() {
		return d_gameMap;
	}

	/**
	 * Sets the game map.
	 *
	 * @param p_gameMap The game map to set
	 */
	public void setGameMap(Map p_gameMap) {
		this.d_gameMap = p_gameMap;
		this.d_gamePlayController.setGameMap(p_gameMap);
	}

	/**
	 * Gets the list of players.
	 *
	 * @return The list of players
	 */
	public List<Player> getPlayers() {
		return d_players;
	}

	/**
	 * Sets the list of players.
	 *
	 * @param p_players The list of players to set
	 */
	public void setPlayers(List<Player> p_players) {
		this.d_players = p_players;
		this.d_gamePlayController.setPlayers(p_players);
	}

	/**
	 * Gets the map file path.
	 *
	 * @return The map file path
	 */
	public String getMapFilePath() {
		return d_mapFilePath;
	}

	/**
	 * Sets the map file path.
	 *
	 * @param p_mapFilePath The map file path to set
	 */
	public void setMapFilePath(String p_mapFilePath) {
		this.d_mapFilePath = p_mapFilePath;
	}

	/**
	 * Checks if the game has started.
	 *
	 * @return true if the game has started, false otherwise
	 */
	public boolean isGameStarted() {
		return d_gameStarted;
	}

	/**
	 * Sets whether the game has started.
	 *
	 * @param p_gameStarted The game started status to set
	 */
	public void setGameStarted(boolean p_gameStarted) {
		this.d_gameStarted = p_gameStarted;
	}

	/**
	 * Gets the current phase of the game.
	 *
	 * @return The current phase
	 */
	public int getCurrentPhase() {
		return d_currentPhase;
	}

	/**
	 * Sets the current phase of the game.
	 *
	 * @param p_currentPhase The current phase to set
	 */
	public void setCurrentPhase(int p_currentPhase) {
		this.d_currentPhase = p_currentPhase;
	}

//    /**
//     * Sets the game phase to the startup phase and handles the command.
//     *
//     * @param p_gameController the game controller managing the game state
//     * @param p_commandParts   the command parts passed as input
//     */
//    public void setStartupPhase(GameController p_gameController, String[] p_commandParts) {
//        // Don't set the phase again here
//        // Just handle the command parts
//        Phase currentState = getCurrentState();
//        currentState.StartPhase(p_gameController, d_players, d_commandPromptView, p_commandParts, d_gameMap);
//    }

	/**
	 * Gets the game logger instance.
	 *
	 * @return The game logger
	 */
	public GameLogger getGameLogger() {
		return d_gameLogger;
	}
}