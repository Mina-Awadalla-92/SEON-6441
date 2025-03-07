package com.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

/**
 * The GameEngine class implements the main game logic and command processing.
 * It manages the different phases of the game: map editing, startup, and main game loop.
 * Handles all command validations and provides feedback on their effects.
 */
public class GameEngine {

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
     * The current player being used.
     */
    private List<Player> d_players;

    /**
     * The current neutral player being used.
     */
    private Player d_neutralPlayer; // For blockade orders

    /**
     * Indicates whether the game has started or not.
     */
    private boolean d_gameStarted;

    /**
     * Represents the current phase of the game.
     * <ul>
     *   <li>0: Map Editing</li>
     *   <li>1: Startup</li>
     *   <li>2: Main Game</li>
     * </ul>
     */
    private int d_currentPhase; // 0: Map Editing, 1: Startup, 2: Main Game

    /**
     * Indicates whether the countries have been assigned to players.
     */
    private boolean d_countriesAssigned = false;

    // Game phases constants

    /**
     * Constant representing the Map Editing phase of the game.
     */
    private static final int MAP_EDITING_PHASE = 0;

    /**
     * Constant representing the Startup phase of the game.
     */
    private static final int STARTUP_PHASE = 1;

    /**
     * Constant representing the Main Game phase of the game.
     */
    private static final int MAIN_GAME_PHASE = 2;

    /**
     * Default constructor that initializes the game engine.
     */
    public GameEngine() {
        this.d_gameMap = new Map();
        this.d_mapLoader = new MapLoader();
        this.d_players = new ArrayList<>();
        this.d_neutralPlayer = new Player("Neutral");
        this.d_gameStarted = false;
        this.d_currentPhase = MAP_EDITING_PHASE;
    }
    
    /**
     * Displays menu for Map Editing Phase
     */
    public void displayMapEditingMenu() {
        System.out.println("\n=== Map Editing Phase Menu ===\n");
        System.out.println("1. editcontinent <args>   - Edit continent details");
        System.out.println("2. editcountry <args>     - Edit country details");
        System.out.println("3. editneighbor <args>    - Edit neighboring territories");
        System.out.println("4. showmap                - Display the current map");
        System.out.println("5. savemap <args>         - Save the current map");
        System.out.println("6. editmap <args>         - Edit the map");
        System.out.println("7. validatemap            - Validate the map");
        System.out.println("8. loadmap <args>         - Load an existing map");
        System.out.println("9. gameplayer <args>      - Setup players and transition to startup phase");
        System.out.println("\nType 'exit' to quit the map editing phase.\n");
    }
    
    /**
     * Displays menu for startup phase
     */
    public void displayStartupMenu() {
        System.out.println("\n=== Startup Phase Menu ===\n");
        System.out.println("1. showmap            - Display the current map");
        System.out.println("2. gameplayer <args>  - Manage game players");
        System.out.println("3. assigncountries    - Assign countries to players");
        System.out.println("4. startgame          - Start the main game");
        System.out.println("5. editmap <args>     - Return to map editing phase");
        System.out.println("6. loadmap <args>     - Load a different map");
        System.out.println("\nType 'exit' to quit the startup phase.\n");
    }

    /**
     * Displays menu for main game
     */
    public void displayMainGameMenu() {
        System.out.println("\n=== Main Game Phase Menu ===\n");
        System.out.println("1. showmap         - Display the current map");
        System.out.println("2. reinforcement   - Handle the reinforcement phase");
        System.out.println("3. issueorder      - Issue an order");
        System.out.println("4. executeorders   - Execute all issued orders");
        System.out.println("5. endturn         - End the current turn, and move to the next one");
        System.out.println("\nType 'exit' to quit the main game phase.\n");
    }

    /**
     * Starts the game and processes user commands.
     * Provides a command prompt that's available throughout the game.
     */
    public void startGame() {
        System.out.println("Welcome to Warzone Game!");

        Scanner l_scanner = new Scanner(System.in);
        boolean l_isMapLoaded = false;

        while (true) {
            
            if (d_currentPhase == MAP_EDITING_PHASE) {
            	
                // Map editing phase
            	displayMapEditingMenu();
            	System.out.print("Enter command: ");
                String l_input = l_scanner.nextLine().trim();
                String[] l_commandParts = l_input.split("\\s+");

                if (l_commandParts.length == 0) {
                    continue;
                }

                String l_command = l_commandParts[0];

                if (l_command.equals("exit")) {
                    System.out.println("Exiting the program.");
                    System.exit(0); // Exit the program
                }
                
                if (!l_isMapLoaded && !l_command.equals("editmap") && !l_command.equals("loadmap")) {
                    System.out.println("You must load/edit a map first using the 'editmap' or 'loadmap' command.");
                } else {
                    l_isMapLoaded = handleMapEditingPhase(l_commandParts, l_command, l_isMapLoaded);
                }
            } else if (d_currentPhase == STARTUP_PHASE) {
                // Startup phase
            	displayStartupMenu();
            	System.out.print("Enter command: ");
                String l_input = l_scanner.nextLine().trim();
                String[] l_commandParts = l_input.split("\\s+");

                if (l_commandParts.length == 0) {
                    continue;
                }

                String l_command = l_commandParts[0];

                if (l_command.equals("exit")) {
                    System.out.println("Exiting the program.");
                    System.exit(0); // Exit the program
                }
                handleStartupPhase(l_commandParts, l_command);
            } else if (d_currentPhase == MAIN_GAME_PHASE) {
                // Main game phase
            	displayMainGameMenu();
            	System.out.print("Enter command: ");
                String l_input = l_scanner.nextLine().trim();
                String[] l_commandParts = l_input.split("\\s+");

                if (l_commandParts.length == 0) {
                    continue;
                }

                String l_command = l_commandParts[0];

                if (l_command.equals("exit")) {
                    System.out.println("Exiting the program.");
                    System.exit(0); // Exit the program
                }
                handleMainGamePhase(l_commandParts, l_command);
            }
        }
    }
    
    /**
     * Handles commands specific to the map editing phase.
     * 
     * @param p_commandParts Array of command components
     * @param p_command The main command
     * @param p_isMapLoaded Flag indicating if a map is loaded
     * @return Updated map loaded status
     */
    private boolean handleMapEditingPhase(String[] p_commandParts, String p_command, boolean p_isMapLoaded) {
        switch (p_command) {
            case "editcontinent":
                handleEditContinent(p_commandParts);
                break;
            case "editcountry":
                handleEditCountry(p_commandParts);
                break;
            case "editneighbor":
                handleEditNeighbor(p_commandParts);
                break;
            case "showmap":
                handleShowMap();
                break;
            case "savemap":
                handleSaveMap(p_commandParts);
                
                break;
            case "editmap":
                p_isMapLoaded = true;
                handleEditMap(p_commandParts);
                break;
            case "validatemap":
                handleValidateMap();
                break;
            case "loadmap":
                p_isMapLoaded = true;
                handleLoadMap(p_commandParts);
                break;
            case "gameplayer":
                // Allow gameplayer command to transition to startup phase
                d_currentPhase = STARTUP_PHASE;
                handleGamePlayer(p_commandParts);
                break;
            default:
                System.out.println("Unknown command: " + p_command);
        }
        return p_isMapLoaded;
    }
    
    /**
     * Handles commands specific to the startup phase.
     * 
     * @param p_commandParts Array of command components
     * @param p_command The main command
     */
    private void handleStartupPhase(String[] p_commandParts, String p_command) {
        switch (p_command) {
            case "showmap":
            	
                handleShowMap();
                break;
            case "gameplayer":
                handleGamePlayer(p_commandParts);
                break;
            case "assigncountries":
            	d_countriesAssigned = true;
                handleAssignCountries();
                break;
            case "startgame":
            	if (!d_countriesAssigned) {
                    System.out.println("Error: You must assign countries first using 'assigncountries' before starting the game.");
                } else {
                    startMainGame();
                }
            	break;
            case "editmap":
                // Allow returning to map editing phase
                d_currentPhase = MAP_EDITING_PHASE;
                handleEditMap(p_commandParts);
                break;
            case "loadmap":
                // Allow loading a different map
                d_currentPhase = MAP_EDITING_PHASE;
                handleLoadMap(p_commandParts);
                break;
            default:
                System.out.println("Unknown command or invalid for current phase: " + p_command);
        }
    }
    
    /**
     * Handles commands specific to the main game phase.
     * 
     * @param p_commandParts Array of command components
     * @param p_command The main command
     */
    private void handleMainGamePhase(String[] p_commandParts, String p_command) {
        switch (p_command) {
            case "showmap":
                handleShowMap();
                break;
            
            case "issueorder":
                handleIssueOrder();
                break;
            case "executeorders":
                handleExecuteOrders();
                break;
            case "endturn":
                handleEndTurn();
                break;
            default:
                System.out.println("Unknown command or invalid for current phase: " + p_command);
        }
    }

    /**
     * Handles the editcontinent command to add or remove continents.
     * Format: editcontinent -add continentID continentvalue -remove continentID
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditContinent(String[] p_commandParts) {
        if (p_commandParts.length < 3) {
            System.out.println("Usage: editcontinent -add continentID continentvalue -remove continentID");
            return;
        }
        
        String l_action = p_commandParts[1];
        
        if (l_action.equals("-add") && p_commandParts.length >= 4) {
            String l_continentID = p_commandParts[2];
            try {
                int l_continentValue = Integer.parseInt(p_commandParts[3]);
                d_gameMap.addContinent(l_continentID, l_continentValue);
                System.out.println("Continent added: " + l_continentID);
            } catch (NumberFormatException e) {
                System.out.println("Invalid continent value: " + p_commandParts[3]);
            }
        } else if (l_action.equals("-remove") && p_commandParts.length >= 3) {
            String l_continentID = p_commandParts[2];
            d_gameMap.removeContinent(l_continentID);
            System.out.println("Continent removed: " + l_continentID);
        } else {
            System.out.println("Usage: editcontinent -add continentID continentvalue -remove continentID");
        }
    }

    /**
     * Handles the editcountry command to add or remove countries.
     * Format: editcountry -add countryID continentID -remove countryID
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditCountry(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            System.out.println("Usage: editcountry -add countryID continentID | editcountry -remove countryID");
            return;
        }
        
        String l_action = p_commandParts[1];
        
        if (l_action.equals("-add")) {
            
            if (p_commandParts.length != 4) {
                System.out.println("Usage: editcountry -add countryID continentID");
                return;
            }
            String l_countryID = p_commandParts[2];
            String l_continentID = p_commandParts[3];
            d_gameMap.addCountry(l_countryID, l_continentID);
            System.out.println("Country added: " + l_countryID);
            
        } else if (l_action.equals("-remove")) {
            
            if (p_commandParts.length != 3) {
                System.out.println("Usage: editcountry -remove countryID");
                return;
            }
            String l_countryID = p_commandParts[2];
            d_gameMap.removeCountry(l_countryID);
            System.out.println("Country removed: " + l_countryID);
            
        } else {
            System.out.println("Invalid action for editcountry.");
        }
    }

    /**
     * Handles the editneighbor command to add or remove connections between countries.
     * Format: editneighbor -add countryID neighborCountryID -remove countryID neighborCountryID
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditNeighbor(String[] p_commandParts) {
        if (p_commandParts.length < 4) {
            System.out.println("Usage: editneighbor -add countryID neighborCountryID -remove countryID neighborCountryID");
            return;
        }
        
        String l_action = p_commandParts[1];
        String l_countryID = p_commandParts[2];
        String l_neighborCountryID = p_commandParts[3];
        
        if (l_action.equals("-add")) {
            d_gameMap.addNeighbor(l_countryID, l_neighborCountryID);
            System.out.println("Neighbor added between: " + l_countryID + " and " + l_neighborCountryID);
        } else if (l_action.equals("-remove")) {
            d_gameMap.removeNeighbor(l_countryID, l_neighborCountryID);
            System.out.println("Neighbor removed between: " + l_countryID + " and " + l_neighborCountryID);
        } else {
            System.out.println("Invalid action for editneighbor.");
        }
    }

    /**
     * Displays the current map.
     * Format: showmap
     */
    private void handleShowMap() {
        System.out.println("\n======== Game Map Overview ========");
        
        // Get the full list of territories from the map.
        List<Territory> allTerritories = d_gameMap.getTerritoryList();
        // Build a list of unique continents.
        List<String> continents = new ArrayList<>();
        for (Territory territory : allTerritories) {
            String continent = territory.getContinent();
            if (!continents.contains(continent)) {
                continents.add(continent);
            }
        }
        
        // Print territories grouped by continent.
        for (String continent : continents) {
            System.out.println("\nContinent: " + continent);
            System.out.println("----------------------------");
            for (Territory territory : allTerritories) {
                if (territory.getContinent().equals(continent)) {
                    System.out.println("Territory: " + territory.getName() 
                            + " | Armies: " + territory.getNumOfArmies());
                    
                    // Display neighbor list for this territory.
                    List<Territory> neighbors = territory.getNeighborList();
                    if (neighbors.isEmpty()) {
                        System.out.println("    Neighbors: None");
                    } else {
                        System.out.print("    --> Neighbors: ");
                        for (Territory neighbor : neighbors) {
                        	System.out.print(neighbor.getName() + " || ");
                        }
                        System.out.println(); // Newline after listing neighbors.
                    }
                }
            }
        }
        System.out.println("=====================================");
        
        // Then display detailed status for each player.
        System.out.println("\nPlayers status:");
        for (Player player : d_players) {
            System.out.println("-------------------------------------------------");
            System.out.println("Player Name: " + player.getName());
            System.out.println("Total Territories Owned: " + player.getOwnedTerritories().size());
            System.out.println("Available Reinforcement Armies: " + player.getNbrOfReinforcementArmies());
            System.out.println("Territory Details:");
            for (Territory territory : player.getOwnedTerritories()) {
                System.out.println("    - " + territory.getName() 
                        + " (" + territory.getNumOfArmies() + " armies)");
                
            }
        }
        System.out.println("-------------------------------------------------");
    }

    /**
     * Handles the savemap command to save the current map to a file.
     * Format: savemap filename
     * 
     * @param p_commandParts Array of command components
     */
    private void handleSaveMap(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            System.out.println("Usage: savemap filename");
            return;
        }
        
        String l_filename = p_commandParts[1];
        d_gameMap.saveToFile(l_filename);
    }

    /**
     * Handles the editmap command to load an existing map for editing or create a new one.
     * Format: editmap filename
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditMap(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            System.out.println("Usage: editmap filename");
            return;
        }
        
        d_mapFilePath = p_commandParts[1];
        d_mapLoader.resetLoadedMap();

        boolean l_isMapValid = d_mapLoader.isValid(d_mapFilePath);
        if (l_isMapValid) {
            d_mapLoader.read(d_mapFilePath);
            d_gameMap = d_mapLoader.getLoadedMap();
            System.out.println(d_mapFilePath + " is loaded successfully.");
        } else {
            System.out.println("The specified map is not exist, a new map is created.");
            d_gameMap = new Map();
        }
    }

    /**
     * Validates the current map.
     * Format: validatemap
     */
    private void handleValidateMap() {
        // First check if there's an in-memory map to validate
        if (d_gameMap != null && !d_gameMap.getTerritoryList().isEmpty()) {
            System.out.println("Validating in-memory map...");
            // For now, we just do a simple check to see if there are territories
            boolean l_valid = d_gameMap.getTerritoryList().size() > 0;
            
            if (l_valid) {
                System.out.println("In-memory map is valid.");
            } else {
                System.out.println("In-memory map is invalid.");
            }
        } else if (d_mapFilePath != null) {
            if (d_mapLoader.isValid(d_mapFilePath)) {
                System.out.println("Map is valid.");
            } else {
                System.out.println("Map is invalid.");
            }
        } else {
            System.out.println("No map loaded to validate.");
        }
    }
    
    /**
     * Handles the loadmap command to load a map for gameplay.
     * Format: loadmap filename
     * 
     * @param p_commandParts Array of command components
     */
    private void handleLoadMap(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            System.out.println("Usage: loadmap filename");
            return;
        }
        
        d_mapFilePath = p_commandParts[1];
        d_mapLoader.resetLoadedMap();

        boolean l_isMapValid = d_mapLoader.isValid(d_mapFilePath);
        if (l_isMapValid) {
            d_mapLoader.read(d_mapFilePath);
            d_gameMap = d_mapLoader.getLoadedMap();
            System.out.println(d_mapFilePath + " is loaded successfully.");
            d_currentPhase = STARTUP_PHASE;
        } else {
            System.out.println("The specified map does not exist or is invalid.");
        }
    }
    
    /**
     * Handles the gameplayer command to add or remove players.
     * Format: gameplayer -add playerName -remove playerName
     * 
     * @param p_commandParts Array of command components
     */
    private void handleGamePlayer(String[] p_commandParts) {
        if (p_commandParts.length < 3) {
            System.out.println("Usage: gameplayer -add playerName OR gameplayer -remove playerName");
            return;
        }
        
        String l_action = p_commandParts[1];
        String l_playerName = p_commandParts[2];
        
        if (l_action.equals("-add")) {
            // Check if player already exists
            boolean l_playerExists = false;
            for (Player l_player : d_players) {
                if (l_player.getName().equals(l_playerName)) {
                    l_playerExists = true;
                    break;
                }
            }
            
            if (!l_playerExists) {
                d_players.add(new Player(l_playerName));
                System.out.println("Player added: " + l_playerName);
            } else {
                System.out.println("Player already exists: " + l_playerName);
            }
        } else if (l_action.equals("-remove")) {
            d_players.removeIf(player -> player.getName().equals(l_playerName));
            System.out.println("Player removed: " + l_playerName);
        } else {
            System.out.println("Invalid action for gameplayer.");
        }
    }
    
    /**
     * Handles the assigncountries command to randomly distribute territories among players.
     * Format: assigncountries
     */
    private void handleAssignCountries() {
        if (d_players.size() < 2) {
            System.out.println("Need at least 2 players to start the game.");
            return;
        }
        
        // Get all territories from the map
        List<Territory> l_territories = d_gameMap.getTerritoryList();
        
        if (l_territories.isEmpty()) {
            System.out.println("No territories in the map. Cannot assign countries.");
            return;
        }
        
        // Shuffle territories for random assignment
        Random l_random = new Random();
        for (int i = l_territories.size() - 1; i > 0; i--) {
            int l_index = l_random.nextInt(i + 1);
            Territory temp = l_territories.get(l_index);
            l_territories.set(l_index, l_territories.get(i));
            l_territories.set(i, temp);
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
        }
        
        System.out.println("Countries assigned to players:");
        for (Player l_player : d_players) {
            System.out.println(l_player.getName() + " owns " + l_player.getOwnedTerritories().size() + " territories.");
        }
        
        System.out.println("Ready to start the game. Use 'startgame' command to begin.");
    }
    
    /**
     * Starts the main game after the startup phase.
     * Format: startgame
     */
    private void startMainGame() {
        if (d_players.size() < 2) {
            System.out.println("Need at least 2 players to start the game.");
            return;
        }
        
        // Initialize the game
        d_gameStarted = true;
        d_currentPhase = MAIN_GAME_PHASE;
        
        System.out.println("Game started! Beginning reinforcement phase.");
        // Start with reinforcement phase
        handleReinforcement();
    }
    
    /**
     * Handles the reinforcement phase of the game.
     * Calculates and assigns reinforcement armies for each player.
     * Format: reinforcement
     */
    private void handleReinforcement() {
        if (!d_gameStarted) {
            System.out.println("Game has not started yet. Use 'startgame' command.");
            return;
        }
        
        System.out.println("\n===== REINFORCEMENT PHASE =====");
        
        // Calculate reinforcements for each player
        for (Player l_player : d_players) {
            // Basic calculation: number of territories divided by 3, minimum 3
            int l_reinforcements = Math.max(3, l_player.getOwnedTerritories().size() / 3);
            
            l_player.setNbrOfReinforcementArmies(l_reinforcements);
            System.out.println(l_player.getName() + " receives " + l_reinforcements + " reinforcement armies.");
        }
        
        System.out.println("\nPlayers have been assigned their armies!\n");
    }
    
    /**
     * Handles the issue order phase of the game.
     * Allows each player to issue deploy orders.
     * Format: issueorder
     */
    private void handleIssueOrder() {
        if (!d_gameStarted) {
            System.out.println("Game has not started yet. Use 'startgame' command.");
            return;
        }
        
        System.out.println("\n===== ISSUE ORDERS PHASE =====");
        
        for (Player l_player : d_players) {
            if (l_player.getNbrOfReinforcementArmies() > 0) {
                System.out.println("\n" + l_player.getName() + "'s turn (" + 
                                  l_player.getNbrOfReinforcementArmies() + " reinforcements left)");
                
                // Display player's territories
                System.out.println("Your territories:");
                List<Territory> l_playerTerritories = l_player.getOwnedTerritories();
                for (int i = 0; i < l_playerTerritories.size(); i++) {
                    Territory t = l_playerTerritories.get(i);
                    System.out.println((i+1) + ". " + t.getName() + " (" + t.getNumOfArmies() + " armies)");
                }
                
                l_player.issueOrder();
            }
        }
        
        System.out.println("\nAll players have issued their orders.\n");
    }
    
    /**
     * Handles the execute orders phase of the game.
     * Executes all orders in round-robin fashion.
     * Format: executeorders
     */
    private void handleExecuteOrders() {
        if (!d_gameStarted) {
            System.out.println("Game has not started yet. Use 'startgame' command.");
            return;
        }
        
        System.out.println("\n===== EXECUTE ORDERS PHASE =====");
        System.out.println("Executing all orders...");
        
        // Loop until all orders are executed
        boolean l_ordersRemaining = true;
        
        while (l_ordersRemaining) {
            l_ordersRemaining = false;
            
            // Each player executes one order
            for (Player l_player : d_players) {
                Order l_nextOrder = l_player.nextOrder();
                
                if (l_nextOrder != null) {
                    System.out.println("\nExecuting order from " + l_player.getName() + ":");
                    l_nextOrder.execute();
                    l_ordersRemaining = true;
                }
            }
        }
        
        System.out.println("\nAll orders executed. Use 'endturn' to end the turn or 'showmap' to see the current state.");
    }
    
    /**
     * Handles the end of a turn.
     * Checks for a winner and either ends the game or starts a new turn.
     * Format: endturn
     */
    private void handleEndTurn() {
        if (!d_gameStarted) {
            System.out.println("Game has not started yet. Use 'startgame' command.");
            return;
        }
        
        // Check for game end condition
        Player l_winner = checkForWinner();
        
        if (l_winner != null) {
            System.out.println("\n*******************************");
            System.out.println("Game Over! " + l_winner.getName() + " wins!");
            System.out.println("*******************************\n");
            
            d_gameStarted = false;
            d_currentPhase = MAP_EDITING_PHASE;
        } else {
            // Start a new turn with reinforcement phase
            System.out.println("\nTurn ended. Starting new turn.");
            handleReinforcement();
        }
    }
    
    /**
     * Checks if there is a winner in the current game state.
     * 
     * @return The winning player, or null if there is no winner yet
     */
    private Player checkForWinner() {
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
}