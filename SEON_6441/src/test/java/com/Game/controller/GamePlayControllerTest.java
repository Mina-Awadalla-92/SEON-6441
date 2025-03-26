package com.Game.controller;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GamePlayControllerTest {

    private GamePlayController gamePlayController;
    private GameController mockGameController;
    private Map mockGameMap;
    private List<Player> mockPlayers;
    private GameView mockGameView;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockGameController = mock(GameController.class);
        mockGameMap = mock(Map.class);
        mockPlayers = new ArrayList<>();
        mockGameView = mock(GameView.class);

        // Set up mock behavior
        when(mockGameController.getView()).thenReturn(mockGameView);

        // Initialize GamePlayController with mocked dependencies
        gamePlayController = new GamePlayController(mockGameController, mockGameMap, mockPlayers);
    }

    @Test
    void testHandleAssignCountries_Success() {
        // Arrange
        List<Territory> mockTerritories = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Territory territory = mock(Territory.class);
            mockTerritories.add(territory);
        }
        when(mockGameMap.getTerritoryList()).thenReturn(mockTerritories);

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        mockPlayers.add(player1);
        mockPlayers.add(player2);

        // Act
        boolean result = gamePlayController.handleAssignCountries();

        // Assert
        assertTrue(result, "handleAssignCountries should return true when successful.");
        assertEquals(3, player1.getOwnedTerritories().size(), "Player1 should own 3 territories.");
        assertEquals(3, player2.getOwnedTerritories().size(), "Player2 should own 3 territories.");
        verify(mockGameView, times(1)).displayMessage("Countries assigned to players:");
    }

    @Test
    void testHandleAssignCountries_NotEnoughPlayers() {
        // Act
        boolean result = gamePlayController.handleAssignCountries();

        // Assert
        assertFalse(result, "handleAssignCountries should return false when there are fewer than 2 players.");
        verify(mockGameView, times(1)).displayError("Need at least 2 players to start the game.");
    }

    @Test
    void testHandleAssignCountries_NoTerritories() {
        // Arrange
        when(mockGameMap.getTerritoryList()).thenReturn(new ArrayList<>());

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        mockPlayers.add(player1);
        mockPlayers.add(player2);

        // Act
        boolean result = gamePlayController.handleAssignCountries();

        // Assert
        assertFalse(result, "handleAssignCountries should return false when there are no territories.");
        verify(mockGameView, times(1)).displayError("No territories in the map. Cannot assign countries.");
    }

    @Test
    void testStartMainGame_Success() {
        // Arrange
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        mockPlayers.add(player1);
        mockPlayers.add(player2);

        // Act
        boolean result = gamePlayController.startMainGame();

        // Assert
        assertTrue(result, "startMainGame should return true when successful.");
        verify(mockGameController, times(1)).setGameStarted(true);
        verify(mockGameController, times(1)).setCurrentPhase(GameController.MAIN_GAME_PHASE);
        verify(mockGameView, times(1)).displayMessage("Game started! Beginning reinforcement phase.");
    }

    @Test
    void testStartMainGame_NotEnoughPlayers() {
        // Act
        boolean result = gamePlayController.startMainGame();

        // Assert
        assertFalse(result, "startMainGame should return false when there are fewer than 2 players.");
        verify(mockGameView, times(1)).displayError("Need at least 2 players to start the game.");
    }

    @Test
    void testHandleReinforcement() {
        // Arrange
        when(mockGameController.isGameStarted()).thenReturn(true);

        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        when(player1.getOwnedTerritories()).thenReturn(new ArrayList<>(List.of(mock(Territory.class), mock(Territory.class), mock(Territory.class))));
        when(player2.getOwnedTerritories()).thenReturn(new ArrayList<>(List.of(mock(Territory.class), mock(Territory.class))));
        mockPlayers.add(player1);
        mockPlayers.add(player2);

        // Act
        gamePlayController.handleReinforcement();

        // Assert
        verify(player1, times(1)).setNbrOfReinforcementArmies(3); // 3 territories -> 3 armies
        verify(player2, times(1)).setNbrOfReinforcementArmies(3); // 2 territories -> minimum 3 armies
        verify(mockGameView, times(1)).displayReinforcementComplete();
    }

    @Test
    void testHandleReinforcement_GameNotStarted() {
        // Arrange
        when(mockGameController.isGameStarted()).thenReturn(false);

        // Act
        gamePlayController.handleReinforcement();

        // Assert
        verify(mockGameView, times(1)).displayError("Game has not started yet. Use 'startgame' command.");
    }
}