package com.Game.controller;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.Game.Phases.IssueOrderPhase;
import com.Game.Phases.Phase;
import com.Game.Phases.PhaseType;
import com.Game.model.CardType;
import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.observer.GameLogger;

/**
 * Controller class responsible for handling gameplay operations. This class
 * processes user commands related to the main game phase, including issuing
 * orders, executing orders, and managing the game turn cycle. In the Command
 * pattern, GamePlayController (as part of the GameEngine) acts as the Client.
 */
public class GamePlayController {

	/**
	 * Message displayed when a command is issued before the game has started.
	 */
	private static final String GAME_NOT_STARTED_MESSAGE = "Game has not started yet. Use 'startgame' command.";

	/**
	 * Reference to the main game controller.
	 */
	private GameController d_gameController;

	/**
	 * The game map being used.
	 */
	private Map d_gameMap;

	/**
	 * The list of players in the game.
	 */
	private List<Player> d_players;

	/**
	 * Game logger for logging game events.
	 */
	private GameLogger d_gameLogger;

	/**
	 * Flag to track if orders have been executed this turn.
	 */
	private boolean d_ordersExecutedThisTurn = false;

	/**
	 * A cryptographically secure pseudo-random number generator (CSPRNG) used for
	 * generating random values.
	 * <p>
	 * This instance of {@link SecureRandom} is designed for use in
	 * security-sensitive applications where strong randomness is required, such as
	 * for generating secure tokens, cryptographic keys, or salt values. It ensures
	 * that the generated random numbers are difficult to predict, even if an
	 * attacker knows part of the state.
	 * </p>
	 * <p>
	 * Note: This {@link SecureRandom} instance should only be used for secure
	 * applications. For non-cryptographic purposes, consider using
	 * {@link java.util.Random}.
	 * </p>
	 */
	private Random d_random = new Random();

	/**
	 * Constructor initializing the controller with necessary references.
	 *
	 * @param p_gameController Reference to the main game controller
	 * @param p_gameMap        The game map
	 * @param p_players        The list of players
	 */
	public GamePlayController(GameController p_gameController, Map p_gameMap, List<Player> p_players) {
		this.d_gameController = p_gameController;
		this.d_gameMap = p_gameMap;
		this.d_players = p_players;

		// Get or create the logger instance
		this.d_gameLogger = GameLogger.getInstance();
		if (this.d_gameLogger == null) {
			// If not initialized yet, create a new instance
			String l_logFilePath = "logs/warzone_game_fallback.log";
			File logsDir = new File("logs");
			if (!logsDir.exists()) {
				logsDir.mkdir();
			}
			this.d_gameLogger = GameLogger.getInstance(l_logFilePath, false);
		}

	}

	/**
	 * Updates the game map reference.
	 *
	 * @param p_gameMap The new game map
	 */
	public void setGameMap(Map p_gameMap) {
		this.d_gameMap = p_gameMap;
	}

	/**
	 * Updates the player list reference.
	 *
	 * @param p_players The new list of players
	 */
	public void setPlayers(List<Player> p_players) {
		this.d_players = p_players;
	}

	/**
	 * Represents the current phase of the game.
	 */
	public Phase d_currentPhase = new IssueOrderPhase();

	/**
	 * Handles a command in the main game phase.
	 *
	 * @param p_commandParts The parts of the command (split by spaces)
	 * @param p_command      The main command (first word)
	 */
	public void handleCommand(String[] p_commandParts, String p_command) {
	    switch (p_command) {
	        case "showmap":
	            d_gameController.getView().displayMap(d_gameMap, d_players);
	            if (d_gameLogger != null) {
	                d_gameLogger.logAction("Map displayed during gameplay");
	            }
	            break;
	        case "issueorder":
	            if (d_ordersExecutedThisTurn) {
	                d_gameController.getView().displayError(
	                        "Orders have already been executed this turn. Please use 'endturn' to proceed to the next turn.");
	                if (d_gameLogger != null) {
	                    d_gameLogger.logAction("Error: Attempted to issue orders after execution phase");
	                }
	                return;
	            }
	            d_gameController.setPhase(PhaseType.ISSUE_ORDER);
	            handleIssueOrder();
	            break;
	        case "executeorders":
	            d_gameController.setPhase(PhaseType.ORDER_EXECUTION);
	            handleExecuteOrders();
	            d_ordersExecutedThisTurn = true; // Set the flag after executing orders
	            break;
	        case "endturn":
	            handleEndTurn();
	            d_ordersExecutedThisTurn = false; // Reset the flag for the new turn
	            break;
	        case "tournament":
	            // Validate the current map before starting tournament
	            if (!validateCurrentMap()) {
	                d_gameController.getView().displayError(
	                        "Current map is invalid. Please load a valid map before starting a tournament.");
	                if (d_gameLogger != null) {
	                    d_gameLogger.logAction("Error: Attempted to start tournament with invalid map");
	                }
	                return;
	            }
	            d_gameController.handleTournamentCommand(p_commandParts);
	            break;
	        default:
	            d_gameController.getView().displayError("Unknown command or invalid for current phase: " + p_command);
	            if (d_gameLogger != null) {
	                d_gameLogger.logAction("Error: Unknown command '" + p_command + "' in main game phase");
	            }
	    }
	}

	/**
	 * Validates the current map loaded in the game.
	 * 
	 * @return true if the map is valid, false otherwise
	 */
	private boolean validateCurrentMap() {
	    if (d_gameMap == null || d_gameMap.getTerritoryList().isEmpty()) {
	        d_gameController.getView().displayError("No map loaded.");
	        return false;
	    }
	    
	    // Validate map connectivity
	    boolean isMapConnected = d_gameMap.mapValidation();
	    if (!isMapConnected) {
	        d_gameController.getView().displayError("Map validation failed: Map is not connected.");
	        return false;
	    }
	    
	    // Validate continent connectivity
	    boolean areContinentsConnected = d_gameMap.continentValidation();
	    if (!areContinentsConnected) {
	        d_gameController.getView().displayError("Map validation failed: One or more continents are not connected.");
	        return false;
	    }
	    
	    return true;
	}

	/**
	 * Handles the reinforcement phase of the game. Calculates and assigns
	 * reinforcement armies for each player.
	 */
	public void handleReinforcement() {
		if (!d_gameController.isGameStarted()) {
			d_gameController.getView().displayError(GAME_NOT_STARTED_MESSAGE);
			d_gameLogger.logAction("Error: Attempted to start reinforcement phase before game started");
			return;
		}

		d_gameController.getView().displayReinforcementPhase();
		d_gameLogger.logPhaseChange("REINFORCEMENT");

		// Calculate reinforcements for each player
		for (Player l_player : d_players) {
			// Basic calculation: number of territories divided by 3, minimum 3
			int l_reinforcements = Math.max(3, l_player.getOwnedTerritories().size() / 3);

			l_player.setNbrOfReinforcementArmies(l_reinforcements);
			d_gameController.getView().displayReinforcementAllocation(l_player.getName(), l_reinforcements);
			d_gameLogger.logAction(
					"Player " + l_player.getName() + " received " + l_reinforcements + " reinforcement armies");
		}

		d_gameController.getView().displayReinforcementComplete();
	}

	/**
	 * Handles the issue order phase of the game. Allows each player to issue deploy orders.
	 * In the Command pattern, this is where Players (Invokers) create Order objects (Commands).
	 */
	private void handleIssueOrder() {
	    if (!d_gameController.isGameStarted()) {
	        d_gameController.getView().displayError(GAME_NOT_STARTED_MESSAGE);
	        d_gameLogger.logAction("Error: Attempted to start issue order phase before game started");
	        return;
	    }

	    d_gameController.getView().displayIssueOrdersPhase();
	    d_gameLogger.logPhaseChange("ISSUE ORDER");

	    d_currentPhase = d_currentPhase.setPhase(PhaseType.ISSUE_ORDER);
	    d_currentPhase.StartPhase(d_gameController, d_players, d_gameController.getCommandPromptView(), null,
	            d_gameMap);

	    d_gameController.getView().displayIssueOrdersComplete();
	    d_gameLogger.logAction("All players have issued their orders");
	}

	/**
	 * Handles the execute orders phase of the game. Executes all orders in
	 * round-robin fashion. In the Command pattern, this is where the GameEngine
	 * (Client) gets Orders from Players and executes them.
	 */
	private void handleExecuteOrders() {
		if (!d_gameController.isGameStarted()) {
			d_gameController.getView().displayError(GAME_NOT_STARTED_MESSAGE);
			d_gameLogger.logAction("Error: Attempted to start execute orders phase before game started");
			return;
		}

		d_gameController.getView().displayExecuteOrdersPhase();
		d_gameLogger.logPhaseChange("ORDER EXECUTION");

		d_currentPhase = d_currentPhase.setPhase(PhaseType.ORDER_EXECUTION);
		d_currentPhase.StartPhase(d_gameController, d_players, d_gameController.getCommandPromptView(), null,
				d_gameMap);

		d_gameController.getView().displayExecuteOrdersComplete();
		d_gameLogger.logAction("All orders have been executed");
	}

	/**
	 * Handles the end of a turn. Checks for a winner and either ends the game or
	 * starts a new turn.
	 */
	private void handleEndTurn() {
		if (!d_gameController.isGameStarted()) {
			d_gameController.getView().displayError(GAME_NOT_STARTED_MESSAGE);
			if (d_gameLogger != null) {
				d_gameLogger.logAction("Error: Attempted to end turn before game started");
			}
			return;
		}

		// Check for game end condition
		Player l_winner = checkForWinner();

		if (l_winner != null) {
			d_gameController.getView().displayWinner(l_winner.getName());
			if (d_gameLogger != null) {
				d_gameLogger.logAction("Game ended. Player " + l_winner.getName() + " is the winner!");
			}

			d_gameController.setGameStarted(false);
			d_gameController.setCurrentPhase(GameController.MAP_EDITING_PHASE);
		} else {
			// Start a new turn with reinforcement phase
			d_gameController.getView().displayEndTurn();
			if (d_gameLogger != null) {
				d_gameLogger.logAction("Turn ended. Starting new turn.");
			}

			// Award cards to players who conquered territories
			System.out.println("\nCards awarding:\n");
			for (Player l_player : d_players) {
				if (l_player.getHasConqueredThisTurn()) {
					CardType[] allCardTypes = CardType.values();

					// Pick a random index
					int l_randomIndex = d_random.nextInt(allCardTypes.length);

					// Get the random card
					CardType randomCard = allCardTypes[l_randomIndex];

					// Add the card to the player's hand
					l_player.addCard(randomCard);

					System.out.println("Player " + l_player.getName() + " was awarded " + randomCard.name());
					if (d_gameLogger != null) {
						d_gameLogger.logAction("Player " + l_player.getName() + " was awarded a " + randomCard.name()
								+ " card for conquering territory");
					}
				}
			}
			System.out.println();

			// Reset player status for the new turn
			for (Player l_player : d_players) {
				l_player.setHasConqueredThisTurn(false);
				l_player.setNegociatedPlayersPerTurn(new ArrayList<>());
			}
			if (d_gameLogger != null) {
				d_gameLogger.logAction("Player conquest and diplomacy statuses reset for new turn");
			}

			// Start reinforcement phase
			handleReinforcement();
		}
	}

	/**
	 * Checks if there is a winner in the current game state.
	 *
	 * @return The winning player, or null if there is no winner yet
	 */
	public Player checkForWinner() {
	    // Check if any player owns all territories
	    for (Player l_player : d_players) {
	        if (l_player.getOwnedTerritories().size() == d_gameMap.getTerritoryList().size()) {
	            return l_player;
	        }
	    }

	    // Check if only one player has territories
	    int l_playersWithTerritories = 0;
	    Player l_lastPlayerWithTerritories = null;

	    for (Player l_player : d_players) {
	        if (l_player.getOwnedTerritories().size() > 0) {
	            l_playersWithTerritories++;
	            l_lastPlayerWithTerritories = l_player;
	        }
	    }

	    if (l_playersWithTerritories == 1) {
	        return l_lastPlayerWithTerritories;
	    }

	    return null; // No winner yet
	}

	/**
	 * Handles the assignment of countries to players. Randomly distributes
	 * territories among players.
	 *
	 * @return true if assignment was successful, false otherwise
	 */
	public boolean handleAssignCountries() {

		if (d_players.size() < 2) {
			d_gameController.getView().displayError("Need at least 2 players to start the game.");
			// Add null check before logging
			if (d_gameLogger != null) {
				d_gameLogger.logAction("Error: Cannot assign countries. Need at least 2 players.");
			}
			return false;
		}

		// Get all territories from the map
		List<Territory> l_territories = d_gameMap.getTerritoryList();

		if (l_territories.isEmpty()) {
			d_gameController.getView().displayError("No territories in the map. Cannot assign countries.");
			// Add null check before logging
			if (d_gameLogger != null) {
				d_gameLogger.logAction("Error: Cannot assign countries. No territories in the map.");
			}
			return false;
		}

		// Shuffle territories for random assignment
		for (int i = l_territories.size() - 1; i > 0; i--) {
			int l_index = d_random.nextInt(i + 1);
			Territory l_temp = l_territories.get(l_index);
			l_territories.set(l_index, l_territories.get(i));
			l_territories.set(i, l_temp);
		}

		// Clear any existing ownership
		for (Player l_player : d_players) {
			l_player.getOwnedTerritories().clear();
		}

		// Assign territories to players
		int l_playerCount = d_players.size();
		for (int i = 0; i < l_territories.size(); i++) {
			Territory l_territory = l_territories.get(i);
			Player l_player = d_players.get(i % l_playerCount);

			l_territory.setOwner(l_player);
			l_player.addTerritory(l_territory);

			// Set initial armies (e.g., 1 per territory)
			l_territory.setNumOfArmies(1);

			d_gameLogger.logAction("Territory " + l_territory.getName() + " assigned to player " + l_player.getName());
		}

		d_gameController.getView().displayMessage("Countries assigned to players:");
		for (Player l_player : d_players) {
			d_gameController.getView().displayMessage(
					l_player.getName() + " owns " + l_player.getOwnedTerritories().size() + " territories.");
		}

		d_gameController.getView().displayMessage("Ready to start the game. Use 'startgame' command to begin.");
		d_gameLogger.logAction("Countries successfully assigned to players");

		// Add null check before logging
		if (d_gameLogger != null) {
			d_gameLogger.logAction("Countries successfully assigned to players");
		}
		return true;
	}

	/**
	 * Starts the main game after the startup phase.
	 *
	 * @return true if the game started successfully, false otherwise
	 */
	public boolean startMainGame() {
		if (d_players.size() < 2) {
			d_gameController.getView().displayError("Need at least 2 players to start the game.");
			d_gameLogger.logAction("Error: Cannot start game. Need at least 2 players.");
			return false;
		}

		// Initialize the game
		d_gameController.setGameStarted(true);
		d_gameController.setCurrentPhase(GameController.MAIN_GAME_PHASE);

		d_gameController.getView().displayMessage("Game started! Beginning reinforcement phase.");
		d_gameLogger.logAction("Main game started");

		// Start with reinforcement phase
		handleReinforcement();
		return true;
	}
}