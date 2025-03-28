package com.Game.controller;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.observer.GameLogger;
import com.Game.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GamePlayControllerTest {

    private GameController mockGameController;
    private Map mockGameMap;
    private List<Player> mockPlayers;
    private GamePlayController gamePlayController;
    GameLogger gameLoggerMock;
    private GameView mockGameView;

    @BeforeEach
    public void setUp() {
        mockGameController = mock(GameController.class);
        mockGameMap = mock(Map.class);
        gamePlayController = mock(GamePlayController.class);
        mockGameView = mock(GameView.class);
        gameLoggerMock = mock(GameLogger.class);

        // Manually initializing mocks in case the auto initialization fails
        mockGameController = mock(GameController.class);
        mockGameMap = mock(Map.class);
        mockPlayers = Arrays.asList(mock(Player.class), mock(Player.class));

        // Initialize the GamePlayController with the mocked dependencies
        gamePlayController = new GamePlayController(mockGameController, mockGameMap, mockPlayers);
    }

    @Test
    public void testHandleCommand_ShowMap() {
        // Make sure mockGameController is initialized properly
        assertNotNull(mockGameController, "mockGameController should not be null");

        when(mockGameController.getView()).thenReturn(mockGameView);

        // Simulate calling the 'showmap' command
        gamePlayController.handleCommand(new String[]{"showmap"}, "showmap");

        // Verify that displayMap() was called on the GameView with the correct parameters
        verify(mockGameView).displayMap(mockGameMap, mockPlayers);
    }

    @Test
    public void testHandleCommand_ShowMap2() {
        when(mockGameController.getView()).thenReturn(mockGameView);  // Make sure getView() is mocked

        // Simulate calling the 'showmap' command
        gamePlayController.handleCommand(new String[]{"showmap"}, "showmap");

        // Verify the expected behavior
        verify(mockGameView).displayMap(mockGameMap, mockPlayers);
    }

    @Test
    public void testHandleCommand_IssueOrder() {
        when(mockGameController.getView()).thenReturn(mockGameView);  // Ensure getView() is mocked

        // Simulate calling the 'issueorder' command when orders have not been executed
        gamePlayController.handleCommand(new String[]{"issueorder"}, "issueorder");

        // Verify that displayError() is called because the game has not started yet
        verify(mockGameView).displayError("Game has not started yet. Use 'startgame' command.");
    }

}

