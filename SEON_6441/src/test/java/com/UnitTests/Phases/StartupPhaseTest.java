package com.UnitTests.Phases;

import com.Game.Phases.StartupPhase;
import com.Game.controller.GameController;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import com.Game.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;


public class StartupPhaseTest {
    private StartupPhase startupPhase;
    private GameController mockGameController;
    private CommandPromptView mockView;
    private Map mockGameMap;
    private List<Player> mockPlayers;
    private GameView mockGameView;


    @BeforeEach
    void setUp() {
        startupPhase = new StartupPhase();
        mockGameController = mock(GameController.class);
        mockGameView = mock(GameView.class); // Mock GameView instead
        mockGameMap = mock(Map.class);
        mockPlayers = mock(List.class);
        mockView = mock(CommandPromptView.class);

        when(mockGameController.getView()).thenReturn(mockGameView);
    }

    @Test
    void testStartPhase_InvalidCommand_ShouldDisplayError() {
        String[] invalidCommand = {"gameplayer"}; // Invalid command for testing

        // Execute the test
        startupPhase.StartPhase(mockGameController, mockPlayers, mockView, invalidCommand, mockGameMap);

        // Verify that the error message is displayed correctly
        verify(mockGameView).displayError("Usage: gameplayer -add playerName OR gameplayer -remove playerName");
    }

    @Test
    void testStartPhase_ValidAddCommand_ShouldHandleGamePlayer() {
        String[] validCommand = {"gameplayer", "-add", "Player1"};

        startupPhase.StartPhase(mockGameController, mockPlayers, mockView, validCommand, mockGameMap);

        verify(mockGameController).handleGamePlayer("-add", "Player1");
    }

    @Test
    void testStartPhase_ValidRemoveCommand_ShouldHandleGamePlayer() {
        String[] validCommand = {"gameplayer", "-remove", "Player1"};

        startupPhase.StartPhase(mockGameController, mockPlayers, mockView, validCommand, mockGameMap);

        verify(mockGameController).handleGamePlayer("-remove", "Player1");
    }
}