package com.Game.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;
import com.Game.view.GameView;

/**
 * A unit test for GameController that uses public setter methods
 * to inject mock dependencies (GameView, CommandPromptView, etc.).
 * No reflection needed.
 */
public class GameControllerTest {

    private GameController gameController;
    private GameView mockView;
    private CommandPromptView mockPromptView;
    private MapEditorController mockMapEditorController;
    private GamePlayController mockGamePlayController;

    @BeforeEach
    void setUp() {
        // 1. Create a real GameController
        gameController = new GameController();

        // 2. Create mock objects for its sub-components
        mockView = mock(GameView.class);
        mockPromptView = mock(CommandPromptView.class);
        mockMapEditorController = mock(MapEditorController.class);
        mockGamePlayController = mock(GamePlayController.class);

        // 3. Use the new public setters to inject mocks
        gameController.setView(mockView);
        gameController.setCommandPromptView(mockPromptView);
        gameController.setMapEditorController(mockMapEditorController);
        gameController.setGamePlayController(mockGamePlayController);
    }

    @Test
    void testConstructorInitialState() {
        // Check defaults from the GameController constructor
        assertEquals(GameController.MAP_EDITING_PHASE, gameController.getCurrentPhase(),
                "Expected default phase to be MAP_EDITING_PHASE");
        assertFalse(gameController.isGameStarted(), "Game should not be started by default");
        assertFalse(gameController.areCountriesAssigned(), "Countries shouldn't be assigned by default");

        assertNotNull(gameController.getPlayers(), "Players list is not null");
        assertTrue(gameController.getPlayers().isEmpty(), "No players initially");

        assertNotNull(gameController.getGameMap(), "Game map should not be null");
    }

    @Test
    void testHandleGamePlayer_AddPlayer() {
        // Start with 0 players
        assertTrue(gameController.getPlayers().isEmpty());

        // Add "Alice"
        gameController.handleGamePlayer("-add", "Alice");
        List<Player> currentPlayers = gameController.getPlayers();

        assertEquals(1, currentPlayers.size());
        assertEquals("Alice", currentPlayers.get(0).getName());

        // The GameView should display a success message
        verify(mockView).displayMessage("Player added: Alice");

        // Try adding "Alice" again => error
        gameController.handleGamePlayer("-add", "Alice");
        assertEquals(1, currentPlayers.size(), 
                "Should still be 1 because 'Alice' already exists");
        verify(mockView).displayError("Player already exists: Alice");
    }

    @Test
    void testHandleGamePlayer_RemovePlayer() {
        // Add "Bob" and "Carol"
        gameController.handleGamePlayer("-add", "Bob");
        gameController.handleGamePlayer("-add", "Carol");
        assertEquals(2, gameController.getPlayers().size());

        // Remove "Bob"
        gameController.handleGamePlayer("-remove", "Bob");
        assertEquals(1, gameController.getPlayers().size());
        assertEquals("Carol", gameController.getPlayers().get(0).getName());

        // Check message
        verify(mockView).displayMessage("Player removed: Bob");
    }

    @Test
    void testHandleGamePlayer_InvalidAction() {
        // Something like "???" is not a valid action
        gameController.handleGamePlayer("???", "Dave");
        assertTrue(gameController.getPlayers().isEmpty());
        verify(mockView).displayError("Invalid action for gameplayer.");
    }

    @Test
    void testSetAndGetMapFilePath() {
        gameController.setMapFilePath("someMap.map");
        assertEquals("someMap.map", gameController.getMapFilePath());
    }

    @Test
    void testSetAndGetGameStarted() {
        gameController.setGameStarted(true);
        assertTrue(gameController.isGameStarted());
        gameController.setGameStarted(false);
        assertFalse(gameController.isGameStarted());
    }

    @Test
    void testSetAndGetCurrentPhase() {
        gameController.setCurrentPhase(GameController.STARTUP_PHASE);
        assertEquals(GameController.STARTUP_PHASE, gameController.getCurrentPhase());
    }

    @Test
    void testSetAndGetCountriesAssigned() {
        gameController.setCountriesAssigned(true);
        assertTrue(gameController.areCountriesAssigned());
        gameController.setCountriesAssigned(false);
        assertFalse(gameController.areCountriesAssigned());
    }

    @Test
    void testSetAndGetGameMap() {
        Map newMap = new Map();
        gameController.setGameMap(newMap);

        assertSame(newMap, gameController.getGameMap(), 
                "Expected same instance that we just set");

        // If inside setGameMap() you call something like
        // gamePlayController.setGameMap(newMap), verify that:
        verify(mockGamePlayController).setGameMap(newMap);
    }

    @Test
    void testSetAndGetPlayers() {
        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        List<Player> newPlayers = List.of(alice, bob);

        gameController.setPlayers(newPlayers);
        assertEquals(2, gameController.getPlayers().size());
        assertSame(alice, gameController.getPlayers().get(0));

        // If inside setPlayers() you call gamePlayController.setPlayers(newPlayers),
        // verify that call happened:
        verify(mockGamePlayController).setPlayers(newPlayers);
    }

    // Typically, we do not directly test startGame() because it loops forever reading commands.
    // If you want to test it, you'd refactor that method or add a "test mode" exit condition.
}
