package com.Game.Phases;

import com.Game.controller.GameController;
import com.Game.observer.GameLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class StartupPhaseTest {

    @Mock
    private GameController gameControllerMock;

    @Mock
    private CommandPromptView commandPromptViewMock;

    @Mock
    private GameLogger gameLoggerMock;

    @Mock
    private Map gameMapMock;

    @Mock
    private Player playerMock1;

    @Mock
    private Player playerMock2;

    private StartupPhase startupPhase;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        startupPhase = new StartupPhase() {}; // Use an anonymous subclass since StartupPhase is abstract
        // Inject the mock logger into the phase's inherited d_gameLogger
        startupPhase.d_gameLogger = gameLoggerMock;

        // Redirect System.out to capture print statements
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testStartPhase_ValidStartGameCommand() {
        // Given: Valid "startgame" command
        String[] commandParts = {"startgame"};

        // When: Start the phase with valid command
        startupPhase.StartPhase(gameControllerMock, Arrays.asList(playerMock1, playerMock2), commandPromptViewMock, commandParts, gameMapMock);

        // Then: The game should transition to the main game phase
        verify(gameControllerMock).setCurrentPhase(GameController.MAIN_GAME_PHASE);
        verify(gameLoggerMock).logAction("Game started. Moving to main game phase.");
    }

    @Test
    public void testStartPhase_ValidGamePlayerCommand_Add() {
        // Given: Valid "gameplayer" command to add a player
        String[] commandParts = {"gameplayer", "-add", "player1"};

        // When: Start the phase with the add command
        startupPhase.StartPhase(gameControllerMock, Arrays.asList(playerMock1, playerMock2), commandPromptViewMock, commandParts, gameMapMock);

        // Then: The controller should handle the "gameplayer" command
        verify(gameControllerMock).handleGamePlayer("-add", "player1");
        verify(gameLoggerMock).logAction("Player management: -add player1");
    }
}