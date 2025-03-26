package com.Game.controller;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.GameView;
import com.Game.view.CommandPromptView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameControllerTest {

    private GameController gameController;
    private GameView mockGameView;
    private CommandPromptView mockCommandPromptView;

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

    @Test
    void testInitialization() {
        // Verify that the GameController initializes correctly
        assertNotNull(gameController.getGameMap(), "Game map should not be null after initialization.");
        assertNotNull(gameController.getPlayers(), "Players list should not be null after initialization.");
        assertEquals(GameController.MAP_EDITING_PHASE, gameController.getCurrentPhase(), "Initial phase should be MAP_EDITING_PHASE.");
    }

    @Test
    void testHandleGamePlayer_AddPlayer() {
        // Test adding a player
        gameController.handleGamePlayer("-add", "Player1");

        List<Player> players = gameController.getPlayers();
        assertEquals(1, players.size(), "Players list should contain one player.");
        assertEquals("Player1", players.get(0).getName(), "Player name should match the added player.");
    }

    @Test
    void testHandleGamePlayer_RemovePlayer() {
        // Add a player first
        gameController.handleGamePlayer("-add", "Player1");

        // Remove the player
        gameController.handleGamePlayer("-remove", "Player1");

        List<Player> players = gameController.getPlayers();
        assertTrue(players.isEmpty(), "Players list should be empty after removing the player.");
    }

    @Test
    void testHandleGamePlayer_AddDuplicatePlayer() {
        // Add a player
        gameController.handleGamePlayer("-add", "Player1");

        // Try adding the same player again
        gameController.handleGamePlayer("-add", "Player1");

        List<Player> players = gameController.getPlayers();
        assertEquals(1, players.size(), "Players list should not allow duplicate players.");
    }

    @Test
    void testSetAndGetGameMap() {
        // Create a new map and set it
        Map newMap = new Map();
        gameController.setGameMap(newMap);

        // Verify the map is set correctly
        assertEquals(newMap, gameController.getGameMap(), "Game map should match the newly set map.");
    }

    @Test
    void testSetAndGetPlayers() {
        // Create a new players list and set it
        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(new Player("Player1"));
        gameController.setPlayers(newPlayers);

        // Verify the players list is set correctly
        assertEquals(newPlayers, gameController.getPlayers(), "Players list should match the newly set list.");
    }

    @Test
    void testSetAndGetCurrentPhase() {
        // Set the current phase to STARTUP_PHASE
        gameController.setCurrentPhase(GameController.STARTUP_PHASE);

        // Verify the current phase is set correctly
        assertEquals(GameController.STARTUP_PHASE, gameController.getCurrentPhase(), "Current phase should match the newly set phase.");
    }

    @Test
    void testSetAndGetGameStarted() {
        // Set the game started flag
        gameController.setGameStarted(true);

        // Verify the flag is set correctly
        assertTrue(gameController.isGameStarted(), "Game started flag should be true.");
    }
}