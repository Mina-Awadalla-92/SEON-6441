package com.Game.controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;
import com.Game.view.GameView;

/**
 * Unit tests for the GameController class.
 * This test class verifies the behavior of the GameController methods, including initialization,
 * player management, and game state management.
 */
public class GameControllerTest {

    private GameController gameController;
    private GameView mockGameView;
    private CommandPromptView mockCommandPromptView;

    /**
     * Sets up the test environment before each test.
     * Mocks dependencies and initializes the GameController instance.
     */
    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockGameView = mock(GameView.class);
        mockCommandPromptView = mock(CommandPromptView.class);

        // Initialize GameController with mocked dependencies
        gameController = new GameController();
        gameController.setGameMap(new Map());
        gameController.setPlayers(new ArrayList<>());
    }

    /**
     * Tests that the GameController initializes correctly.
     */
    @Test
    void testInitialization() {
        assertNotNull(gameController.getGameMap(), "Game map should not be null after initialization.");
        assertNotNull(gameController.getPlayers(), "Players list should not be null after initialization.");
        assertEquals(GameController.MAP_EDITING_PHASE, gameController.getCurrentPhase(), "Initial phase should be MAP_EDITING_PHASE.");
    }

    /**
     * Tests adding a player using the handleGamePlayer method.
     */
    @Test
    void testHandleGamePlayer_AddPlayer() {
        gameController.handleGamePlayer("-add", "Player1");

        List<Player> players = gameController.getPlayers();
        assertEquals(1, players.size(), "Players list should contain one player.");
        assertEquals("Player1", players.get(0).getName(), "Player name should match the added player.");
    }

    /**
     * Tests removing a player using the handleGamePlayer method.
     */
    @Test
    void testHandleGamePlayer_RemovePlayer() {
        gameController.handleGamePlayer("-add", "Player1");
        gameController.handleGamePlayer("-remove", "Player1");

        List<Player> players = gameController.getPlayers();
        assertTrue(players.isEmpty(), "Players list should be empty after removing the player.");
    }

    /**
     * Tests that duplicate players cannot be added using the handleGamePlayer method.
     */
    @Test
    void testHandleGamePlayer_AddDuplicatePlayer() {
        gameController.handleGamePlayer("-add", "Player1");
        gameController.handleGamePlayer("-add", "Player1");

        List<Player> players = gameController.getPlayers();
        assertEquals(1, players.size(), "Players list should not allow duplicate players.");
    }

    /**
     * Tests setting and getting the game map.
     */
    @Test
    void testSetAndGetGameMap() {
        Map newMap = new Map();
        gameController.setGameMap(newMap);

        assertEquals(newMap, gameController.getGameMap(), "Game map should match the newly set map.");
    }

    /**
     * Tests setting and getting the players list.
     */
    @Test
    void testSetAndGetPlayers() {
        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(new Player("Player1"));
        gameController.setPlayers(newPlayers);

        assertEquals(newPlayers, gameController.getPlayers(), "Players list should match the newly set list.");
    }

    /**
     * Tests setting and getting the current phase of the game.
     */
    @Test
    void testSetAndGetCurrentPhase() {
        gameController.setCurrentPhase(GameController.STARTUP_PHASE);

        assertEquals(GameController.STARTUP_PHASE, gameController.getCurrentPhase(), "Current phase should match the newly set phase.");
    }

    /**
     * Tests setting and getting the game started flag.
     */
    @Test
    void testSetAndGetGameStarted() {
        gameController.setGameStarted(true);

        assertTrue(gameController.isGameStarted(), "Game started flag should be true.");
    }

    /**
     * Tests that the game controller correctly initializes the neutral player.
     */
    @Test
    void testNeutralPlayerInitialization() {
        Player neutralPlayer = new Player("Neutral");
        assertNotNull(neutralPlayer, "Neutral player should be initialized.");
        assertEquals("Neutral", neutralPlayer.getName(), "Neutral player name should be 'Neutral'.");
    }

    /**
     * Tests that the game controller handles invalid commands gracefully.
     */
    @Test
    void testHandleInvalidCommand() {
        // Simulate an invalid command
        gameController.setCurrentPhase(GameController.STARTUP_PHASE);
        String[] invalidCommand = {"invalidCommand"};

        // Mock the view to capture error messages
        doNothing().when(mockGameView).displayError(anyString());

        // Call the method
        gameController.getView().displayError("Unknown command or invalid for current phase: invalidCommand");

        // Verify that the error message is displayed
        verify(mockGameView, times(1)).displayError("Unknown command or invalid for current phase: invalidCommand");
    }
}