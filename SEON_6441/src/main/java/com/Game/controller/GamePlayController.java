package com.Game.controller;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.Game.Phases.IssueOrderPhase;
import com.Game.Phases.Phase;
import com.Game.Phases.PhaseType;
import com.Game.model.*;
import com.Game.utils.MapLoader;
import com.Game.utils.SingleGameUtil;
import com.Game.model.CardType;
import com.Game.observer.GameLogger;

import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
			case "savegame":
				handleSaveGame(p_commandParts);
				d_ordersExecutedThisTurn = false; // Reset the flag for the new turn
				break;
	        default:
	            d_gameController.getView().displayError("Unknown command or invalid for current phase: " + p_command);
	            if (d_gameLogger != null) {
	                d_gameLogger.logAction("Error: Unknown command '" + p_command + "' in main game phase");
	            }
	    }
	}

	/**
	 * Handles the 'save game' command by validating the input and saving the current game state.
	 *
	 * <p>If the command format is incorrect, an error message is displayed.
	 * Otherwise, the game state is saved to the specified filename.</p>
	 *
	 * @param p_commandParts An array of strings containing the command and its arguments.
	 */
	private void handleSaveGame(String[] p_commandParts)
	{
		if (p_commandParts.length != 2) {
			d_gameController.getView().displayError("Usage: savegame filename");
			return;
		}

		String l_filename = p_commandParts[1];
		d_gameMap.saveGameState(l_filename,  d_players);
	}

	/**
	 * Handles loading a saved game from a file and assigns the game state, including map and players.
	 * It reads the map file, loads the map, validates it, and assigns players and their territories.
	 *
	 * @param p_commandParts An array containing the command and the map file name.
	 */
	public void handleLoadSavedGame(String[] p_commandParts)
	{
		if (p_commandParts.length != 2) {
			d_gameController.getView().displayError("Usage: loadgame filename");
			return;
		}
		MapLoader d_mapLoader = new MapLoader();
		String l_mapFilePath = p_commandParts[1];
		d_gameController.setMapFilePath(l_mapFilePath);
		d_mapLoader.resetLoadedMap();

		BufferedReader l_reader = null;
		boolean l_isMapExist = false;
		l_reader = d_mapLoader.isMapExist(l_mapFilePath);
		if (l_reader != null) {
			l_isMapExist = true;
		}

		if(l_isMapExist) {
			boolean l_isMapInitiallyValid = d_mapLoader.isValid(l_mapFilePath);
			if (l_isMapInitiallyValid) {
				d_mapLoader.read(l_mapFilePath);
				d_gameMap = d_mapLoader.getLoadedMap();
				d_gameController.setGameMap(d_gameMap);

				if(d_mapLoader.validateMap()) {
					d_gameController.getView().displayMessage(l_mapFilePath + " is loaded successfully.");
					d_gameController.setCurrentPhase(GameController.MAIN_GAME_PHASE);

					List<Player> l_loadedPlayers = loadPlayersFromFile(l_mapFilePath);
					d_gameController.setPlayers(l_loadedPlayers);

					assignTerritoriesToPlayersFromFile(l_mapFilePath, l_loadedPlayers);

					startMainGame();
				}
			}
		} else {
			d_gameController.getView().displayError("The specified map does not exist.");
			d_gameMap = new Map();
			d_gameController.setGameMap(d_gameMap);
		}

	}

	/**
	 * Assigns territories to players from a map file, including the territory owner and the number of armies.
	 * This method reads the file, parses the territory owner and army count for each player, and sets the corresponding
	 * ownership and army count on the territories.
	 *
	 * @param p_filePath The path to the map file that contains the territory ownership and army count details.
	 * @param p_players A list of players that are to be assigned territories from the file.
	 *
	 * @throws IOException If an error occurs while reading the file.
	 */
	private void assignTerritoriesToPlayersFromFile(String p_filePath, List<Player> p_players) {
		BufferedReader l_reader = null;

		try {
			File l_file = new File(p_filePath);
			if (l_file.exists()) {
				l_reader = new BufferedReader(new FileReader(l_file));
			} else {
				String l_resourcePath = p_filePath;
				if (!l_resourcePath.contains("LoadingMaps/") && !l_resourcePath.contains("LoadingMaps\\")) {
					l_resourcePath = "LoadingMaps/" + l_resourcePath;
				}

				InputStream l_inputStream = getClass().getClassLoader().getResourceAsStream(l_resourcePath);
				if (l_inputStream == null) {
					d_gameController.getView().displayError("Map file not found in resources: " + l_resourcePath);
					return;
				}

				l_reader = new BufferedReader(new InputStreamReader(l_inputStream));
			}

			String l_line;
			boolean l_isTerritorySection = false;

			while ((l_line = l_reader.readLine()) != null) {
				l_line = l_line.trim();
				if (l_line.isEmpty()) continue;

				if (l_line.equalsIgnoreCase("[Territory Owner]")) {
					l_isTerritorySection = true;
					continue;
				}

				if (l_isTerritorySection) {
					if (l_line.startsWith("[")) break;

					// Example: p1 owns Ontario with 5 armies
					Pattern pattern = Pattern.compile("^(.+) owns (.+?) with (\\d+) armies$");
					Matcher matcher = pattern.matcher(l_line);

					if (matcher.matches()) {
						String l_playerName = matcher.group(1).trim();
						String l_territoryName = matcher.group(2).trim();
						int l_armyCount = Integer.parseInt(matcher.group(3));

						Player l_player = p_players.stream()
								.filter(p -> p.getName().equalsIgnoreCase(l_playerName))
								.findFirst()
								.orElse(null);

						if (l_player != null) {
							Territory l_territory = d_gameMap.getTerritoryByName(l_territoryName);
							if (l_territory != null) {
								l_territory.setOwner(l_player);
								l_territory.setNumOfArmies(l_armyCount);
								l_player.addTerritory(l_territory); // if you have this helper
							}
						}
					}
				}
			}
		} catch (IOException e) {
			d_gameController.getView().displayError("Error reading territory owner info: " + e.getMessage());
		} finally {
			try {
				if (l_reader != null) {
					l_reader.close();
				}
			} catch (IOException e) {
				// ignore
			}
		}
	}

	/**
	 * Loads player data from a specified file. Each player’s name and type are read from the file and used
	 * to create player objects, which are then added to a list.
	 *
	 * The file format is expected to contain a section titled "[Players]" followed by lines in the format:
	 * "playerName playerType". The player type is used to instantiate the appropriate player subclass (e.g., HumanPlayer).
	 *
	 * @param p_filePath The path to the file that contains player information.
	 * @return A list of Player objects created from the data in the file, or null if the file could not be loaded.
	 *
	 * @throws IOException If an error occurs while reading the file.
	 */
	private List<Player> loadPlayersFromFile(String p_filePath) {
		List<Player> l_players = new ArrayList<>();
		boolean l_isPlayerSection = false;

		BufferedReader l_reader = null;

		try {
			File l_file = new File(p_filePath);
			if (l_file.exists()) {
				// File exists locally, open it for reading
				l_reader = new BufferedReader(new FileReader(l_file));
			} else {
				// Try with LoadingMaps prefix
				String l_resourcePath = p_filePath;
				if (!l_resourcePath.contains("LoadingMaps/") && !l_resourcePath.contains("LoadingMaps\\")) {
					l_resourcePath = "LoadingMaps/" + l_resourcePath;
				}

				InputStream l_inputStream = getClass().getClassLoader().getResourceAsStream(l_resourcePath);
				if (l_inputStream == null) {
					return null;
				}

				// Resource found, create a reader
				l_reader = new BufferedReader(new InputStreamReader(l_inputStream));
			}

			String l_line;
			while ((l_line = l_reader.readLine()) != null) {
				l_line = l_line.trim();
				if (l_line.isEmpty()) continue;

				if (l_line.equalsIgnoreCase("[Players]")) {
					l_isPlayerSection = true;
					continue;
				}

				if (l_isPlayerSection) {
					if (l_line.startsWith("[")) break;

					String[] l_parts = l_line.split("\\s+");
					if (l_parts.length == 2) {
						String l_name = l_parts[0];
						String l_rawType = l_parts[1];
						String l_type = l_rawType.replace("Player", "").toLowerCase();

						Player l_player;
						switch (l_type) {
							case "human":
								l_player = new HumanPlayer(l_name, l_type);
								break;
							case "aggressive":
								l_player = new AggressivePlayer(l_name, l_type);
								break;
							case "benevolent":
								l_player = new BenevolentPlayer(l_name, l_type);
								break;
							case "random":
								l_player = new RandomPlayer(l_name, l_type);
								break;
							default:
								l_player = new HumanPlayer(l_name, "human"); // fallback
								break;
						}

						l_players.add(l_player);
					}
				}
			}
		} catch (IOException e) {
			d_gameController.getView().displayError("Error reading player info from file: " + e.getMessage());
		} finally {
			try {
				if (l_reader != null) {
					l_reader.close();
				}
			} catch (IOException e) {
			}
		}

		return l_players;
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
     * Handles the end of a turn in automatic mode.
     * This method ensures card distribution and player status reset.
     */
    private void handleEndTurn() {
        System.out.println("\nAuto: Ending turn and preparing next turn...");

        // Award cards to players who conquered territories
        for (Player player : d_players) {
            if (player.getHasConqueredThisTurn()) {
                // Pick a random card type
                com.Game.model.CardType[] allCardTypes = com.Game.model.CardType.values();
                int randomIndex = d_random.nextInt(allCardTypes.length);
                com.Game.model.CardType randomCard = allCardTypes[randomIndex];

                player.addCard(randomCard);
                System.out.println("Auto: Player " + player.getName() + " was awarded a " + randomCard.name() + " card");
            }
        }

        // Reset player status for the new turn
        for (Player player : d_players) {
            player.setHasConqueredThisTurn(false);
            player.setNegociatedPlayersPerTurn(new ArrayList<>());
        }

        // Start reinforcement for next turn
        handleReinforcement();
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
     * Starts the main game with additional automatic mode handling.
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

        // If no human players, start automatic game loop
        if (shouldRunAutomatically()) {
            runAutomaticGameLoop();
        }

        return true;
    }
    
    /**
     * Checks if the game should proceed automatically based on player types.
     * 
     * @return true if no human players are present, false otherwise
     */
    public boolean shouldRunAutomatically() {
        return d_players.stream()
                .noneMatch(player -> player instanceof HumanPlayer);
    }

    /**
     * Prepares players for single game mode.
     * 
     * @param mapFile The map file being used
     * @param playerStrategies List of player strategies
     * @return List of created players
     */
    public List<Player> prepareSingleGamePlayers(String mapFile, List<String> playerStrategies) {
        // Generate unique names for players
        List<String> playerNames = SingleGameUtil.generatePlayerNames(playerStrategies);
        
        // Create players based on strategies
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerStrategies.size(); i++) {
            String strategy = playerStrategies.get(i);
            String playerName = playerNames.get(i);
            
            try {
                Player player = SingleGameUtil.createPlayerByStrategy(strategy, playerName);
                players.add(player);
            } catch (IllegalArgumentException e) {
                d_gameController.getView().displayError(
                    "Failed to create player with strategy: " + strategy
                );
            }
        }
        
        return players;
    }

    /**
     * Runs a single game in automatic mode.
     * Continues until a winner is determined or max turns are reached.
     * 
     * @param maxTurns Maximum number of turns for the game
     * @return The name of the winner or "Draw"
     */
    public String runAutomaticSingleGame(int maxTurns) {
        // Track current turn
        int currentTurn = 0;
        
        while (currentTurn < maxTurns) {
            // Reinforcement phase
            handleReinforcement();
            
            // Issue orders phase
            handleIssueOrder();
            
            // Execute orders phase
            handleExecuteOrders();
            
            // Check for winner
            Player winner = checkForWinner();
            if (winner != null) {
                return winner.getName();
            }
            
            // End turn and prepare for next
            handleEndTurn();
            
            currentTurn++;
        }
        
        // If no winner after max turns
        return "Draw";
    }

    /**
     * Provides a summary of the game state.
     * 
     * @return A map containing game statistics
     */
    public java.util.Map<String, Object> getGameStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        // Total territories
        int totalTerritories = d_gameMap.getTerritoryList().size();
        stats.put("TotalTerritories", totalTerritories);
        
        // Player territory distribution
        java.util.Map<String, Integer> territoryDistribution = d_players.stream()
            .collect(Collectors.toMap(
                Player::getName, 
                p -> p.getOwnedTerritories().size()
            ));
        stats.put("TerritoryDistribution", territoryDistribution);
        
        // Player army distribution
        java.util.Map<String, Integer> armyDistribution = d_players.stream()
            .collect(Collectors.toMap(
                Player::getName, 
                p -> p.getOwnedTerritories().stream()
                    .mapToInt(t -> t.getNumOfArmies())
                    .sum()
            ));
        stats.put("ArmyDistribution", armyDistribution);
        
        return stats;
    }

    /**
     * Provides a detailed log of the game's progress.
     * 
     * @return A list of significant game events
     */
    public List<String> getGameProgressLog() {
        List<String> progressLog = new ArrayList<>();
        
        // Log initial game state
        progressLog.add("Game Started: " + 
            d_players.stream()
                .map(Player::getName)
                .collect(Collectors.joining(", "))
        );
        
        // You can expand this method to capture more detailed game events
        // For example, territory conquests, significant battles, etc.
        
        return progressLog;
    }
    
    /**
     * Runs the game automatically when no human players are present.
     * Continues until a winner is determined or manual intervention is needed.
     */
    private void runAutomaticGameLoop() {
        new Thread(() -> {
            while (d_gameController.isGameStarted()) {
                try {
                    // Issue orders for all players
                    handleIssueOrder();

                    // Execute orders
                    handleExecuteOrders();

                    // Check for winner
                    Player winner = checkForWinner();
                    if (winner != null) {
                        d_gameController.getView().displayWinner(winner.getName());
                        d_gameController.setGameStarted(false);
                        break;
                    }

                    // End turn and prepare for next turn
                    handleEndTurn();

                    // Small delay to prevent overwhelming the system
                    Thread.sleep(500);
                } catch (Exception e) {
                    d_gameLogger.logAction("Error in automatic game loop: " + e.getMessage());
                    break;
                }
            }
        }).start();
    }

}