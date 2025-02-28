package com.Game.controller;

import com.Game.model.game.GameState;
import com.Game.view.GameView;
import com.Game.model.map.Map;
import com.Game.model.map.Territory;
import com.Game.model.map.MapLoader;

/**
 * Controller for map editing phase commands.
 */
public class MapEditorController {
    private GameState d_gameState;
    private GameView d_gameView;
    private MapLoader d_mapLoader;
    private String d_mapFilePath;
    
    /**
     * Constructor initializing the map editor controller.
     * 
     * @param p_gameState The game state.
     * @param p_gameView The game view.
     */
    public MapEditorController(GameState p_gameState, GameView p_gameView) {
        this.d_gameState = p_gameState;
        this.d_gameView = p_gameView;
        this.d_mapLoader = new MapLoader();
    }
    
    /**
     * Handles commands for the map editing phase.
     * 
     * @param p_commandParts The parts of the command.
     * @return True if the map is loaded, false otherwise.
     */
    public boolean handleCommand(String[] p_commandParts) {
        if (p_commandParts.length == 0) {
            return false;
        }
        
        String l_command = p_commandParts[0];
        boolean l_isMapLoaded = false;
        
        switch (l_command) {
            case "editcontinent":
                handleEditContinent(p_commandParts);
                l_isMapLoaded = true;
                break;
            case "editcountry":
                handleEditCountry(p_commandParts);
                l_isMapLoaded = true;
                break;
            case "editneighbor":
                handleEditNeighbor(p_commandParts);
                l_isMapLoaded = true;
                break;
            case "showmap":
                handleShowMap();
                l_isMapLoaded = true;
                break;
            case "savemap":
                handleSaveMap(p_commandParts);
                // After saving a map, transition to the startup phase
                d_gameState.setCurrentPhase(GameState.STARTUP_PHASE);
                d_gameView.showMessage("Entering Startup Phase. Use 'gameplayer' to add players.");
                l_isMapLoaded = true;
                break;
            case "editmap":
                l_isMapLoaded = handleEditMap(p_commandParts);
                break;
            case "validatemap":
                handleValidateMap();
                l_isMapLoaded = true;
                break;
            case "loadmap":
                l_isMapLoaded = handleLoadMap(p_commandParts);
                break;
            default:
                d_gameView.showMessage("Unknown command for Map Editor: " + l_command);
        }
        
        return l_isMapLoaded;
    }
    
    /**
     * Handles the editcontinent command to add or remove continents.
     * Format: editcontinent -add continentID continentvalue -remove continentID
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditContinent(String[] p_commandParts) {
        if (p_commandParts.length < 3) {
            d_gameView.showMessage("Usage: editcontinent -add continentID continentvalue -remove continentID");
            return;
        }
        
        String l_action = p_commandParts[1];
        
        if (l_action.equals("-add") && p_commandParts.length >= 4) {
            String l_continentID = p_commandParts[2];
            try {
                int l_continentValue = Integer.parseInt(p_commandParts[3]);
                d_gameState.getGameMap().addContinent(l_continentID, l_continentValue);
                d_gameView.showMessage("Continent added: " + l_continentID);
            } catch (NumberFormatException e) {
                d_gameView.showMessage("Invalid continent value: " + p_commandParts[3]);
            }
        } else if (l_action.equals("-remove") && p_commandParts.length >= 3) {
            String l_continentID = p_commandParts[2];
            d_gameState.getGameMap().removeContinent(l_continentID);
            d_gameView.showMessage("Continent removed: " + l_continentID);
        } else {
            d_gameView.showMessage("Invalid action for editcontinent.");
        }
    }

    /**
     * Handles the editcountry command to add or remove countries.
     * Format: editcountry -add countryID continentID -remove countryID
     * 
     * @param p_commandParts Array of command components
     */
    private void handleEditCountry(String[] p_commandParts) {
        if (p_commandParts.length < 4) {
            d_gameView.showMessage("Usage: editcountry -add countryID continentID -remove countryID");
            return;
        }
        
        String l_action = p_commandParts[1];
        String l_countryID = p_commandParts[2];
        
        if (l_action.equals("-add")) {
            String l_continentID = p_commandParts[3];
            d_gameState.getGameMap().addCountry(l_countryID, l_continentID);
            d_gameView.showMessage("Country added: " + l_countryID);
        } else if (l_action.equals("-remove")) {
            d_gameState.getGameMap().removeCountry(l_countryID);
            d_gameView.showMessage("Country removed: " + l_countryID);
        } else {
            d_gameView.showMessage("Invalid action for editcountry.");
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
            d_gameView.showMessage("Usage: editneighbor -add countryID neighborCountryID -remove countryID neighborCountryID");
            return;
        }
        
        String l_action = p_commandParts[1];
        String l_countryID = p_commandParts[2];
        String l_neighborCountryID = p_commandParts[3];
        
        if (l_action.equals("-add")) {
            d_gameState.getGameMap().addNeighbor(l_countryID, l_neighborCountryID);
            d_gameView.showMessage("Neighbor added between: " + l_countryID + " and " + l_neighborCountryID);
        } else if (l_action.equals("-remove")) {
            d_gameState.getGameMap().removeNeighbor(l_countryID, l_neighborCountryID);
            d_gameView.showMessage("Neighbor removed between: " + l_countryID + " and " + l_neighborCountryID);
        } else {
            d_gameView.showMessage("Invalid action for editneighbor.");
        }
    }

    /**
     * Displays the current map.
     * Format: showmap
     */
    private void handleShowMap() {
        d_gameView.showMap(d_gameState.getGameMap().toString());
    }

    /**
     * Handles the savemap command to save the current map to a file.
     * Format: savemap filename
     * 
     * @param p_commandParts Array of command components
     */
    private void handleSaveMap(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            d_gameView.showMessage("Usage: savemap filename");
            return;
        }
        
        String l_filename = p_commandParts[1];
        d_gameState.getGameMap().saveToFile(l_filename);
    }

    /**
     * Handles the editmap command to load an existing map for editing or create a new one.
     * Format: editmap filename
     * 
     * @param p_commandParts Array of command components
     * @return True if the map is loaded, false otherwise.
     */
    private boolean handleEditMap(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            d_gameView.showMessage("Usage: editmap filename");
            return false;
        }
        
        d_mapFilePath = p_commandParts[1];
        d_mapLoader.resetLoadedMap();

        boolean l_isMapValid = d_mapLoader.isValid(d_mapFilePath);
        if (l_isMapValid) {
            d_mapLoader.read(d_mapFilePath);
            d_gameState.setGameMap(d_mapLoader.getLoadedMap());
            d_gameView.showMessage(d_mapFilePath + " is loaded successfully.");
            return true;
        } else {
            d_gameView.showMessage("The specified map is not exist, a new map is created.");
            d_gameState.setGameMap(new Map());
            return true;
        }
    }

    /**
     * Validates the current map.
     * Format: validatemap
     */
    private void handleValidateMap() {
        Map l_gameMap = d_gameState.getGameMap();
        
        // First check if there's an in-memory map to validate
        if (l_gameMap != null && !l_gameMap.getTerritoryList().isEmpty()) {
            d_gameView.showMessage("Validating in-memory map...");
            // For now, we just do a simple check to see if there are territories
            boolean l_valid = l_gameMap.getTerritoryList().size() > 0;
            
            if (l_valid) {
                d_gameView.showMessage("In-memory map is valid.");
            } else {
                d_gameView.showMessage("In-memory map is invalid.");
            }
        } else if (d_mapFilePath != null) {
            if (d_mapLoader.isValid(d_mapFilePath)) {
                d_gameView.showMessage("Map is valid.");
            } else {
                d_gameView.showMessage("Map is invalid.");
            }
        } else {
            d_gameView.showMessage("No map loaded to validate.");
        }
    }
    
    /**
     * Handles the loadmap command to load a map for gameplay.
     * Format: loadmap filename
     * 
     * @param p_commandParts Array of command components
     * @return True if the map is loaded, false otherwise.
     */
    private boolean handleLoadMap(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            d_gameView.showMessage("Usage: loadmap filename");
            return false;
        }
        
        d_mapFilePath = p_commandParts[1];
        d_mapLoader.resetLoadedMap();

        boolean l_isMapValid = d_mapLoader.isValid(d_mapFilePath);
        if (l_isMapValid) {
            d_mapLoader.read(d_mapFilePath);
            d_gameState.setGameMap(d_mapLoader.getLoadedMap());
            d_gameView.showMessage(d_mapFilePath + " is loaded successfully.");
            d_gameState.setCurrentPhase(GameState.STARTUP_PHASE);
            d_gameView.showMessage("Entering Startup Phase. Use 'gameplayer' to add players.");
            return true;
        } else {
            d_gameView.showMessage("The specified map does not exist or is invalid.");
            return false;
        }
    }
}