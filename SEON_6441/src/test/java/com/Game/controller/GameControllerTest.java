package com.Game.controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.utils.MapLoader;
import com.Game.view.GameView;
import com.Game.view.CommandPromptView;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the controller components of the game.
 */
@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {
    
    @Mock private GameView mockGameView;
    @Mock private CommandPromptView mockCommandPromptView;
    @Mock private MapLoader mockMapLoader;
    
    private GameController d_gameController;
    private Map d_gameMap;
    private List<Player> d_players;
    
    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void setUp() {
        // Create a real map for testing
        d_gameMap = new Map();
        d_gameMap.addContinent("TestContinent", 5);
        d_gameMap.addCountry("TestCountry1", "TestContinent");
        d_gameMap.addCountry("TestCountry2", "TestContinent");
        d_gameMap.addNeighbor("TestCountry1", "TestCountry2");
        
        // Set up mock map loader
//        when(mockMapLoader.getLoadedMap()).thenReturn(d_gameMap);
//        when(mockMapLoader.validateMap()).thenReturn(true);
        
        // Create players
        d_players = new ArrayList<>();
        d_players.add(new Player("TestPlayer1"));
        d_players.add(new Player("TestPlayer2"));
        
        // Initialize controllers with real components where needed
        d_gameController = new GameController();
        d_gameController.setGameMap(d_gameMap);
        d_gameController.setPlayers(d_players);
    }
    
    /**
     * Tests the GameController constructor.
     */
    @Test
    public void testGameControllerConstructor() {
        GameController controller = new GameController();
        assertNotNull("GameController should be created", controller);
        assertNotNull("GameMap should be initialized", controller.getGameMap());
        assertNotNull("Players list should be initialized", controller.getPlayers());
        assertTrue("Players list should be empty initially", controller.getPlayers().isEmpty());
    }
    
    /**
     * Tests the handleGamePlayer method.
     */
    @Test
    public void testHandleGamePlayer() {
        // Create a fresh controller without preset players
        GameController controller = new GameController();
        
        // Test adding a player
        controller.handleGamePlayer("-add", "TestPlayer");
        assertEquals("Controller should have 1 player", 1, controller.getPlayers().size());
        assertEquals("Player name should be correct", "TestPlayer", controller.getPlayers().get(0).getName());
        
        // Test adding a duplicate player
        controller.handleGamePlayer("-add", "TestPlayer");
        assertEquals("Controller should still have just 1 player", 1, controller.getPlayers().size());
        
        // Test removing a player
        controller.handleGamePlayer("-remove", "TestPlayer");
        assertTrue("Players list should be empty after removal", controller.getPlayers().isEmpty());
        
        // Test invalid action
        controller.handleGamePlayer("-invalid", "TestPlayer");
        assertTrue("Players list should still be empty after invalid action", controller.getPlayers().isEmpty());
    }
    
    /**
     * Tests setter and getter methods.
     */
    @Test
    public void testSetterGetterMethods() {
        GameController controller = new GameController();
        
        // Test Map
        Map newMap = new Map();
        controller.setGameMap(newMap);
        assertSame("getGameMap should return the set map", newMap, controller.getGameMap());
        
        // Test Players
        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(new Player("NewPlayer"));
        controller.setPlayers(newPlayers);
        assertSame("getPlayers should return the set players list", newPlayers, controller.getPlayers());
        
        // Test MapFilePath
        String mapFilePath = "test/path/map.map";
        controller.setMapFilePath(mapFilePath);
        assertEquals("getMapFilePath should return the set path", mapFilePath, controller.getMapFilePath());
        
        // Test GameStarted
        controller.setGameStarted(true);
        assertTrue("isGameStarted should return true", controller.isGameStarted());
        
        // Test CurrentPhase
        controller.setCurrentPhase(GameController.STARTUP_PHASE);
        assertEquals("getCurrentPhase should return the set phase", GameController.STARTUP_PHASE, controller.getCurrentPhase());
    }
    
    /**
     * Tests the MapEditorController's handleCommand method.
     */
    @Test
    public void testMapEditorControllerHandleCommand() {
        // Create controller with mock components
        MapEditorController mapEditorController = new MapEditorController(d_gameController, d_gameMap, mockMapLoader);
        
        // Test adding a continent
        boolean result = mapEditorController.handleCommand(
            new String[]{"editcontinent", "-add", "NewContinent", "3"}, "editcontinent", true);
        assertTrue("handleCommand should return true", result);
        
        // Test adding a country
        result = mapEditorController.handleCommand(
            new String[]{"editcountry", "-add", "NewCountry", "NewContinent"}, "editcountry", true);
        assertTrue("handleCommand should return true", result);
        
        // Test adding a neighbor
        result = mapEditorController.handleCommand(
            new String[]{"editneighbor", "-add", "TestCountry1", "NewCountry"}, "editneighbor", true);
        assertTrue("handleCommand should return true", result);
        
        // Test showmap
        result = mapEditorController.handleCommand(new String[]{"showmap"}, "showmap", true);
        assertTrue("handleCommand should return true", result);
    }
    
    /**
     * Tests the GamePlayController's handleAssignCountries method.
     */
    @Test
    public void testHandleAssignCountries() {
        // Set up the controller with a real map and players
        GamePlayController gamePlayController = new GamePlayController(d_gameController, d_gameMap, d_players);
        
        // Execute the method
        boolean result = gamePlayController.handleAssignCountries();
        
        // Verify the result
        assertTrue("handleAssignCountries should succeed", result);
        
        // Check that territories were assigned
        List<Territory> territories = d_gameMap.getTerritoryList();
        for (Territory territory : territories) {
            assertNotNull("Territory should have an owner", territory.getOwner());
            assertTrue("Territory should be owned by one of the test players", 
                      d_players.contains(territory.getOwner()));
        }
        
        // Check that players were assigned territories
        for (Player player : d_players) {
            assertFalse("Player should own territories", player.getOwnedTerritories().isEmpty());
        }
    }
    
    /**
     * Tests the GamePlayController's startMainGame method.
     */
    @Test
    public void testStartMainGame() {
        // Use a real GameController
        GameController realController = new GameController();
        realController.setGameMap(d_gameMap);
        realController.setPlayers(d_players);

        GamePlayController gamePlayController = new GamePlayController(realController, d_gameMap, d_players);

        // Execute the method
        boolean result = gamePlayController.startMainGame();

        // Verify the result
        assertTrue("startMainGame should succeed with valid setup", result);

        // Verify that the real controller state has changed
        assertTrue(realController.isGameStarted());
        assertEquals(GameController.MAIN_GAME_PHASE, realController.getCurrentPhase());
    }

    
    /**
     * Tests handleReinforcement, handling both valid and invalid cases.
     */
    @Test
    public void testHandleReinforcement() {
        // Set up controller with a started game
        GameController mockController = mock(GameController.class);
        when(mockController.isGameStarted()).thenReturn(true);
        when(mockController.getView()).thenReturn(mockGameView);
        
        // Add territories to players
        for (int i = 0; i < 6; i++) {
            Territory territory = new Territory("Territory" + i, "TestContinent", 1);
            d_players.get(0).addTerritory(territory);
        }
        
        for (int i = 6; i < 9; i++) {
            Territory territory = new Territory("Territory" + i, "TestContinent", 1);
            d_players.get(1).addTerritory(territory);
        }
        
        GamePlayController gamePlayController = new GamePlayController(mockController, d_gameMap, d_players);
        
        // Execute the method
        gamePlayController.handleReinforcement();
        
        // Verify reinforcements were calculated correctly
        assertEquals("Player with 6 territories should get 3 reinforcements", 
                    3, d_players.get(0).getNbrOfReinforcementArmies());
        assertEquals("Player with 3 territories should get minimum 3 reinforcements", 
                    3, d_players.get(1).getNbrOfReinforcementArmies());
    }
}