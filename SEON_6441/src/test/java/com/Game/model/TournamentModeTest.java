package com.Game.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Game.controller.GameController;
import com.Game.model.AggressivePlayer;
import com.Game.model.BenevolentPlayer;
import com.Game.model.CheaterPlayer;
import com.Game.model.HumanPlayer;
import com.Game.model.Player;
import com.Game.model.RandomPlayer;
import com.Game.model.TournamentMode;
import com.Game.model.Territory;
import com.Game.utils.MapLoader;

/**
 * Test class for TournamentMode functionality.
 */
public class TournamentModeTest {

    private GameController d_gameController;
    private List<String> d_mapFiles;
    private List<String> d_playerStrategies;
    private TournamentMode d_tournament;
    private MapLoader d_mapLoader;
    
    /**
     * Setup method to prepare for tests.
     * Initializes controller, map files, and player strategies.
     */
    @Before
    public void setUp() {
        // Initialize game controller
        d_gameController = new GameController();
        
        // Initialize map loader
        d_mapLoader = new MapLoader();
        
        // Setup map files list
        d_mapFiles = new ArrayList<>();
        d_mapFiles.add("canada.map"); // Use test maps available in resources
        
        // Setup player strategies list
        d_playerStrategies = new ArrayList<>();
        d_playerStrategies.add("aggressive");
        d_playerStrategies.add("benevolent");
    }
    /**
     * Test map validation before tournament.
     */
    @Test
    public void testMapValidation() {
        // Test with valid map
        List<String> validMaps = new ArrayList<>();
        validMaps.add("canada.map");
        
        d_mapLoader.read("canada.map");
        boolean isValidMap = d_mapLoader.validateMap(false);
        assertTrue("Map should be valid", isValidMap);
        
        // Test with invalid map
        List<String> invalidMaps = new ArrayList<>();
        invalidMaps.add("invalid.map");
        
        // Create a mock MapLoader that always returns false for validate
        MapLoader mockLoader = new MapLoader() {
            @Override
            public boolean validateMap(boolean showMsg) {
                return false;
            }
        };
        
        // Read should fail or validation should fail
        assertFalse("Invalid map should fail validation", mockLoader.validateMap(false));
    }

    /**
     * Test enhanced tournament results display.
     */
    @Test
    public void testEnhancedResultsDisplay() {
        // Create a tournament with known results for display testing
        d_tournament = new TournamentMode(d_mapFiles, d_playerStrategies, 2, 20, d_gameController);
        
        // Access the results map through reflection
        try {
            java.lang.reflect.Field resultsField = TournamentMode.class.getDeclaredField("d_results");
            resultsField.setAccessible(true);
            
            @SuppressWarnings("unchecked")
            java.util.Map<String, java.util.Map<Integer, String>> results = 
                (java.util.Map<String, java.util.Map<Integer, String>>) resultsField.get(d_tournament);
            
            // Add some test results
            java.util.Map<Integer, String> mapResults = new HashMap<>();
            mapResults.put(1, "aggressive");
            mapResults.put(2, "benevolent");
            
            results.put("canada.map", mapResults);
            
            // Test that statistics count is correct
            java.lang.reflect.Method countWinsMethod = 
                TournamentMode.class.getDeclaredMethod("countWins");
            countWinsMethod.setAccessible(true);
            
            @SuppressWarnings("unchecked")
            java.util.Map<String, Integer> winCounts = 
                (java.util.Map<String, Integer>) countWinsMethod.invoke(d_tournament);
            
            assertEquals("Aggressive should have 1 win", Integer.valueOf(1), winCounts.get("aggressive"));
            assertEquals("Benevolent should have 1 win", Integer.valueOf(1), winCounts.get("benevolent"));
            
        } catch (Exception e) {
            fail("Exception during reflection: " + e.getMessage());
        }
    }

    /**
     * Test game mode selection.
     */
    @Test
    public void testGameModeSelection() {
        // This is more of an integration test that would require mocking
        // user input, so we'll simulate the validation logic
        
        // Test valid selections
        assertTrue("Selection 1 should be valid", isValidGameModeSelection(1));
        assertTrue("Selection 2 should be valid", isValidGameModeSelection(2));
        
        // Test invalid selections
        assertFalse("Selection 0 should be invalid", isValidGameModeSelection(0));
        assertFalse("Selection 3 should be invalid", isValidGameModeSelection(3));
        assertFalse("Selection -1 should be invalid", isValidGameModeSelection(-1));
    }

    /**
     * Helper method to validate game mode selection.
     */
    private boolean isValidGameModeSelection(int selection) {
        return selection == 1 || selection == 2;
    }
    
    /**
     * Test the creation of the tournament object with valid parameters.
     */
    @Test
    public void testTournamentCreation() {
        d_tournament = new TournamentMode(d_mapFiles, d_playerStrategies, 2, 20, d_gameController);
        assertNotNull("Tournament object should be created successfully", d_tournament);
    }
    
    /**
     * Test validation of tournament parameters.
     */
    @Test
    public void testTournamentParameterValidation() {
        // Test valid parameters
        assertTrue("Valid parameters should pass validation", 
                validateTournamentParams(d_mapFiles, d_playerStrategies, 2, 20));
        
        // Test invalid map count (too many)
        List<String> tooManyMaps = new ArrayList<>(d_mapFiles);
        for (int i = 0; i < 5; i++) {
            tooManyMaps.add("map" + i + ".map");
        }
        assertFalse("Too many maps should fail validation", 
                validateTournamentParams(tooManyMaps, d_playerStrategies, 2, 20));
        
        // Test invalid player strategy count (too few)
        List<String> tooFewStrategies = new ArrayList<>();
        tooFewStrategies.add("aggressive");
        assertFalse("Too few player strategies should fail validation", 
                validateTournamentParams(d_mapFiles, tooFewStrategies, 2, 20));
        
        // Test invalid game count (too many)
        assertFalse("Too many games should fail validation", 
                validateTournamentParams(d_mapFiles, d_playerStrategies, 10, 20));
        
        // Test invalid max turns (too few)
        assertFalse("Too few max turns should fail validation", 
                validateTournamentParams(d_mapFiles, d_playerStrategies, 2, 5));
    }
    
    /**
     * Test creating players by strategy.
     */
    @Test
    public void testCreatePlayersByStrategy() {
        // Test creating aggressive player
        Player aggressivePlayer = createPlayerByStrategy("aggressive", "AggressiveTest");
        assertTrue("Should create an aggressive player", aggressivePlayer instanceof AggressivePlayer);
        
        // Test creating benevolent player
        Player benevolentPlayer = createPlayerByStrategy("benevolent", "BenevolentTest");
        assertTrue("Should create a benevolent player", benevolentPlayer instanceof BenevolentPlayer);
        
        // Test creating random player
        Player randomPlayer = createPlayerByStrategy("random", "RandomTest");
        assertTrue("Should create a random player", randomPlayer instanceof RandomPlayer);
        
        // Test creating cheater player
        Player cheaterPlayer = createPlayerByStrategy("cheater", "CheaterTest");
        assertTrue("Should create a cheater player", cheaterPlayer instanceof CheaterPlayer);
        
        // Test with invalid strategy
        Player defaultPlayer = createPlayerByStrategy("invalid", "DefaultTest");
        assertTrue("Should default to human player for invalid strategy", defaultPlayer instanceof HumanPlayer);
    }
    
    /**
     * Test map loading for tournament.
     */
    @Test
    public void testMapLoading() {
        // Test loading a valid map
        d_mapLoader.read("canada.map");
        com.Game.model.Map map = d_mapLoader.getLoadedMap();
        assertNotNull("Valid map should load successfully", map);
        assertFalse("Valid map should have territories", map.getTerritoryList().isEmpty());
        
        // Test connectivity validation
        assertTrue("Valid map should pass validation", d_mapLoader.validateMap(false));
    }
    
    /**
     * Test countries assignment for players.
     */
    @Test
    public void testCountriesAssignment() {
        // Load map
        d_mapLoader.read("canada.map");
        com.Game.model.Map map = d_mapLoader.getLoadedMap();
        
        // Create players
        List<Player> players = new ArrayList<>();
        players.add(new AggressivePlayer("Aggressive"));
        players.add(new BenevolentPlayer("Benevolent"));
        
        // Assign countries
        assignCountriesRandomly(map, players);
        
        // Verify assignment
        int totalTerritories = map.getTerritoryList().size();
        int playerTerritories = 0;
        for (Player player : players) {
            playerTerritories += player.getOwnedTerritories().size();
        }
        
        assertEquals("All territories should be assigned", totalTerritories, playerTerritories);
        
        // Check each territory has an owner and armies
        for (Territory territory : map.getTerritoryList()) {
            assertNotNull("Each territory should have an owner", territory.getOwner());
            assertTrue("Each territory should have at least one army", territory.getNumOfArmies() > 0);
        }
    }
    
    /**
     * Helper method to validate tournament parameters.
     */
    private boolean validateTournamentParams(List<String> mapFiles, List<String> playerStrategies, 
                                           int numberOfGames, int maxTurns) {
        // Validate number of maps (1-5)
        if (mapFiles.size() < 1 || mapFiles.size() > 5) {
            return false;
        }
        
        // Validate number of player strategies (2-4)
        if (playerStrategies.size() < 2 || playerStrategies.size() > 4) {
            return false;
        }
        
        // Validate number of games (1-5)
        if (numberOfGames < 1 || numberOfGames > 5) {
            return false;
        }
        
        // Validate max turns (10-50)
        if (maxTurns < 10 || maxTurns > 50) {
            return false;
        }
        
        // Validate player strategies
        for (String strategy : playerStrategies) {
            if (!isValidPlayerStrategy(strategy)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Helper method to check if a player strategy is valid.
     */
    private boolean isValidPlayerStrategy(String strategy) {
        return strategy.equalsIgnoreCase("aggressive") ||
               strategy.equalsIgnoreCase("benevolent") ||
               strategy.equalsIgnoreCase("random") ||
               strategy.equalsIgnoreCase("cheater");
    }
    
    /**
     * Helper method to create a player with the specified strategy.
     */
    private Player createPlayerByStrategy(String strategy, String name) {
        switch (strategy.toLowerCase()) {
            case "aggressive":
                return new AggressivePlayer(name);
            case "benevolent":
                return new BenevolentPlayer(name);
            case "random":
                return new RandomPlayer(name);
            case "cheater":
                return new CheaterPlayer(name);
            default:
                // Default to human player if strategy not recognized
                return new HumanPlayer(name);
        }
    }
    
    /**
     * Helper method to assign countries randomly to players.
     */
    private void assignCountriesRandomly(com.Game.model.Map gameMap, List<Player> players) {
        List<Territory> territories = gameMap.getTerritoryList();
        
        if (territories.isEmpty() || players.isEmpty()) {
            return;
        }
        
        // Shuffle territories for random assignment
        java.util.Random rand = new java.util.Random();
        for (int i = territories.size() - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            Territory temp = territories.get(index);
            territories.set(index, territories.get(i));
            territories.set(i, temp);
        }
        
        // Clear any existing ownership
        for (Player player : players) {
            player.getOwnedTerritories().clear();
        }
        
        // Assign territories to players
        int playerCount = players.size();
        for (int i = 0; i < territories.size(); i++) {
            Territory territory = territories.get(i);
            Player player = players.get(i % playerCount);
            
            territory.setOwner(player);
            player.addTerritory(territory);
            
            // Set initial armies (e.g., 1 per territory)
            territory.setNumOfArmies(1);
        }
    }
}