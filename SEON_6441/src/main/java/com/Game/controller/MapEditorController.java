package com.Game.controller;

import com.Game.model.Map;
import com.Game.model.Territory;
import com.Game.utils.MapLoader;
import com.Game.view.GameView;
import com.Game.observer.LogEntryBuffer;

import java.io.BufferedReader;

/**
 * Controller class responsible for handling map editing operations.
 * This class processes user commands related to map editing and interacts with the model and view.
 */
public class MapEditorController {
    
    /**
     * Reference to the main game controller.
     */
    private GameController d_gameController;
    
    /**
     * The game map being edited.
     */
    private Map d_gameMap;
    
    /**
     * The map loader used for loading and validating maps.
     */
    private MapLoader d_mapLoader;
    
    /**
     * Reference to the log entry buffer for logging actions.
     */
    private LogEntryBuffer d_logEntryBuffer;
    
    /**
     * Constructor initializing the controller with necessary references.
     * 
     * @param p_gameController Reference to the main game controller
     * @param p_gameMap The game map to edit
     * @param p_mapLoader The map loader for file operations
     */
    public MapEditorController(GameController p_gameController, Map p_gameMap, MapLoader p_mapLoader) {
        this.d_gameController = p_gameController;
        this.d_gameMap = p_gameMap;
        this.d_mapLoader = p_mapLoader;
        this.d_logEntryBuffer = p_gameController.getLogEntryBuffer();
    }
    
    /**
     * Handles a command in the map editing phase.
     * 
     * @param p_commandParts The parts of the command (split by spaces)
     * @param p_command The main command (first word)
     * @param p_isMapLoaded Flag indicating if a map is currently loaded
     * @return Updated map loaded status
     */
    public boolean handleCommand(String[] p_commandParts, String p_command, boolean p_isMapLoaded) {
        boolean l_isMapLoaded = p_isMapLoaded;
        
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
                d_gameController.getView().displayMap(d_gameMap, d_gameController.getPlayers());
                d_logEntryBuffer.logAction("Displayed map in map editor phase");
                break;
            case "savemap":
                handleSaveMap(p_commandParts);
                break;
            case "editmap":
                l_isMapLoaded = true;
                handleEditMap(p_commandParts);
                break;
            case "validatemap":
                handleValidateMap();
                break;
            case "loadmap":
                l_isMapLoaded = true;
                handleLoadMap(p_commandParts);
                break;
            case "gameplayer":
                // Transition to startup phase
                d_gameController.setCurrentPhase(GameController.STARTUP_PHASE);
                handleGamePlayer(p_commandParts);
                break;
            default:
                d_gameController.getView().displayError("Unknown command: " + p_command);
                d_logEntryBuffer.logAction("Unknown command rejected in map editor phase: " + p_command);
        }
        return l_isMapLoaded;
    }
    
    /**
     * Handles the editcontinent command to add or remove continents.
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditContinent(String[] p_commandParts) {
        if (p_commandParts.length < 3) {
            d_gameController.getView().displayError("Usage: editcontinent -add continentID continentvalue -remove continentID");
            d_logEntryBuffer.logAction("Invalid editcontinent command - insufficient parameters");
            return;
        }
        
        String l_action = p_commandParts[1];
        
        if (l_action.equals("-add") && p_commandParts.length >= 4) {
            String l_continentID = p_commandParts[2];
            try {
                int l_continentValue = Integer.parseInt(p_commandParts[3]);
                d_gameMap.addContinent(l_continentID, l_continentValue);
                d_gameController.getView().displayMessage("Continent added: " + l_continentID);
                d_logEntryBuffer.logAction("Added continent: " + l_continentID + " with value " + l_continentValue);
            } catch (NumberFormatException e) {
                d_gameController.getView().displayError("Invalid continent value: " + p_commandParts[3]);
                d_logEntryBuffer.logAction("Failed to add continent: invalid value " + p_commandParts[3]);
            }
        } else if (l_action.equals("-remove") && p_commandParts.length >= 3) {
            String l_continentID = p_commandParts[2];
            d_gameMap.removeContinent(l_continentID);
            d_gameController.getView().displayMessage("Continent removed: " + l_continentID);
            d_logEntryBuffer.logAction("Removed continent: " + l_continentID);
        } else {
            d_gameController.getView().displayError("Usage: editcontinent -add continentID continentvalue -remove continentID");
            d_logEntryBuffer.logAction("Invalid editcontinent command - incorrect format");
        }
    }
    
    /**
     * Handles the editcountry command to add or remove countries.
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditCountry(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            d_gameController.getView().displayError("Usage: editcountry -add countryID continentID | editcountry -remove countryID");
            d_logEntryBuffer.logAction("Invalid editcountry command - insufficient parameters");
            return;
        }
        
        String l_action = p_commandParts[1];
        
        if (l_action.equals("-add")) {
            
            if (p_commandParts.length != 4) {
                d_gameController.getView().displayError("Usage: editcountry -add countryID continentID");
                d_logEntryBuffer.logAction("Invalid editcountry add command - incorrect format");
                return;
            }
            String l_countryID = p_commandParts[2];
            String l_continentID = p_commandParts[3];
            d_gameMap.addCountry(l_countryID, l_continentID);
            d_gameController.getView().displayMessage("Country added: " + l_countryID);
            d_logEntryBuffer.logAction("Added country: " + l_countryID + " to continent " + l_continentID);
            
        } else if (l_action.equals("-remove")) {
            
            if (p_commandParts.length != 3) {
                d_gameController.getView().displayError("Usage: editcountry -remove countryID");
                d_logEntryBuffer.logAction("Invalid editcountry remove command - incorrect format");
                return;
            }
            String l_countryID = p_commandParts[2];
            d_gameMap.removeCountry(l_countryID);
            d_gameController.getView().displayMessage("Country removed: " + l_countryID);
            d_logEntryBuffer.logAction("Removed country: " + l_countryID);
            
        } else {
            d_gameController.getView().displayError("Invalid action for editcountry.");
            d_logEntryBuffer.logAction("Invalid editcountry action: " + l_action);
        }
    }
    
    /**
     * Handles the editneighbor command to add or remove connections between countries.
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditNeighbor(String[] p_commandParts) {
        if (p_commandParts.length < 4) {
            d_gameController.getView().displayError("Usage: editneighbor -add countryID neighborCountryID -remove countryID neighborCountryID");
            d_logEntryBuffer.logAction("Invalid editneighbor command - insufficient parameters");
            return;
        }
        
        String l_action = p_commandParts[1];
        String l_countryID = p_commandParts[2];
        String l_neighborCountryID = p_commandParts[3];
        
        if (l_action.equals("-add")) {
            d_gameMap.addNeighbor(l_countryID, l_neighborCountryID);
            d_gameController.getView().displayMessage("Neighbor added between: " + l_countryID + " and " + l_neighborCountryID);
            d_logEntryBuffer.logAction("Added neighbor connection: " + l_countryID + " ↔ " + l_neighborCountryID);
        } else if (l_action.equals("-remove")) {
            d_gameMap.removeNeighbor(l_countryID, l_neighborCountryID);
            d_gameController.getView().displayMessage("Neighbor removed between: " + l_countryID + " and " + l_neighborCountryID);
            d_logEntryBuffer.logAction("Removed neighbor connection: " + l_countryID + " ↔ " + l_neighborCountryID);
        } else {
            d_gameController.getView().displayError("Invalid action for editneighbor.");
            d_logEntryBuffer.logAction("Invalid editneighbor action: " + l_action);
        }
    }
    
    /**
     * Handles the savemap command to save the current map to a file.
     * 
     * @param p_commandParts Array of command components
     */
    private void handleSaveMap(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            d_gameController.getView().displayError("Usage: savemap filename");
            d_logEntryBuffer.logAction("Invalid savemap command - missing filename");
            return;
        }
        
        String l_filename = p_commandParts[1];
        d_gameMap.saveToFile(l_filename);
        d_logEntryBuffer.logAction("Map saved to file: " + l_filename);
    }
    
    /**
     * Handles the editmap command to load an existing map for editing or create a new one.
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditMap(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            d_gameController.getView().displayError("Usage: editmap filename");
            d_logEntryBuffer.logAction("Invalid editmap command - missing filename");
            return;
        }
        
        String l_mapFilePath = p_commandParts[1];
        d_mapLoader.resetLoadedMap();
        d_logEntryBuffer.logAction("Attempting to edit map: " + l_mapFilePath);

        // Check if map exists
        BufferedReader l_reader = null;
        boolean l_isMapExist = false;
        l_reader = d_mapLoader.isMapExist(l_mapFilePath);
        if (l_reader != null) {
            l_isMapExist = true;
        }

        if(l_isMapExist) {
            d_logEntryBuffer.logAction("Map file exists, loading for editing: " + l_mapFilePath);
            boolean l_isMapInitiallyValid = d_mapLoader.isValid(l_mapFilePath);

            if (l_isMapInitiallyValid) {
                d_mapLoader.read(l_mapFilePath);
                d_gameMap = d_mapLoader.getLoadedMap();
                d_gameController.setGameMap(d_gameMap);

                if(d_mapLoader.validateMap()) {
                    d_gameController.getView().displayMessage(l_mapFilePath + " is loaded successfully.");
                    d_logEntryBuffer.logAction("Map loaded and validated successfully: " + l_mapFilePath);
                } else {
                    d_logEntryBuffer.logAction("Map loaded but failed validation: " + l_mapFilePath);
                }
            } else {
                d_logEntryBuffer.logAction("Map file exists but is invalid: " + l_mapFilePath);
            }
        } else {
            d_gameController.getView().displayMessage("The specified map is not exist, a new map is created.");
            d_logEntryBuffer.logAction("Map file does not exist, creating new map: " + l_mapFilePath);
            d_gameMap = new Map();
            d_gameController.setGameMap(d_gameMap);
        }
    }
    
    /**
     * Validates the current map.
     */
    private void handleValidateMap() {
        String l_mapFilePath = d_gameController.getMapFilePath();
        d_logEntryBuffer.logAction("Validating map: " + (l_mapFilePath != null ? l_mapFilePath : "current map"));

        if (l_mapFilePath != null && d_gameMap != null) {
            if (d_mapLoader.isValid(l_mapFilePath)) {
                boolean l_isValid = d_mapLoader.validateMap();
                d_logEntryBuffer.logAction("Map validation result: " + (l_isValid ? "valid" : "invalid"));
            } else {
                d_gameController.getView().displayError("The map is invalid.");
                d_logEntryBuffer.logAction("Map validation failed: invalid map format");
            }
        } else {
            d_gameController.getView().displayError("No map loaded to validate.");
            d_logEntryBuffer.logAction("Map validation failed: no map loaded");
        }
    }
    
    /**
     * Handles the loadmap command to load a map for gameplay.
     * 
     * @param p_commandParts Array of command components
     */
    private void handleLoadMap(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            d_gameController.getView().displayError("Usage: loadmap filename");
            d_logEntryBuffer.logAction("Invalid loadmap command - missing filename");
            return;
        }
        
        String l_mapFilePath = p_commandParts[1];
        d_gameController.setMapFilePath(l_mapFilePath);
        d_mapLoader.resetLoadedMap();
        d_logEntryBuffer.logAction("Attempting to load map: " + l_mapFilePath);

        BufferedReader l_reader = null;
        boolean l_isMapExist = false;
        l_reader = d_mapLoader.isMapExist(l_mapFilePath);
        if (l_reader != null) {
            l_isMapExist = true;
        }

        if(l_isMapExist) {
            d_logEntryBuffer.logAction("Map file exists, loading: " + l_mapFilePath);
            boolean l_isMapInitiallyValid = d_mapLoader.isValid(l_mapFilePath);
            if (l_isMapInitiallyValid) {
                d_mapLoader.read(l_mapFilePath);
                d_gameMap = d_mapLoader.getLoadedMap();
                d_gameController.setGameMap(d_gameMap);

                if(d_mapLoader.validateMap()) {
                    d_gameController.getView().displayMessage(l_mapFilePath + " is loaded successfully.");
                    d_logEntryBuffer.logAction("Map loaded and validated successfully: " + l_mapFilePath);
                    d_gameController.setCurrentPhase(GameController.STARTUP_PHASE);
                } else {
                    d_logEntryBuffer.logAction("Map loaded but failed validation: " + l_mapFilePath);
                }
            } else {
                d_logEntryBuffer.logAction("Map file exists but is invalid: " + l_mapFilePath);
            }
        } else {
            d_gameController.getView().displayError("The specified map does not exist.");
            d_logEntryBuffer.logAction("Map file does not exist: " + l_mapFilePath);
            d_gameMap = new Map();
            d_gameController.setGameMap(d_gameMap);
        }
    }
    
    /**
     * Handles the gameplayer command to add or remove players.
     * 
     * @param p_commandParts Array of command components
     */
    private void handleGamePlayer(String[] p_commandParts) {
        if (p_commandParts.length < 3) {
            d_gameController.getView().displayError("Usage: gameplayer -add playerName OR gameplayer -remove playerName");
            d_logEntryBuffer.logAction("Invalid gameplayer command - insufficient parameters");
            return;
        }
        
        String l_action = p_commandParts[1];
        String l_playerName = p_commandParts[2];
        
        d_logEntryBuffer.logAction("Handling gameplayer command: " + l_action + " " + l_playerName);
        d_gameController.handleGamePlayer(l_action, l_playerName);
    }
}