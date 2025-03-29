package com.Game.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.Game.observer.GameLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.GameView;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GamePlayControllerTest {

    @Mock
    private GameController mockGameController;
    @Mock
    private Map mockGameMap;

    @Mock
    private List<Player> mockPlayers;

    @Mock
    private GamePlayController gamePlayController;
    @Mock
    private GameLogger gameLoggerMock;

    @Mock
    private GameView mockGameView;

    @Before
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
    public void testHandleCommand_IssueOrder() {
        when(mockGameController.getView()).thenReturn(mockGameView);  // Ensure getView() is mocked

        // Simulate calling the 'issueorder' command when orders have not been executed
        gamePlayController.handleCommand(new String[]{"issueorder"}, "issueorder");

        // Verify that displayError() is called because the game has not started yet
        verify(mockGameView).displayError("Game has not started yet. Use 'startgame' command.");
    }

}

