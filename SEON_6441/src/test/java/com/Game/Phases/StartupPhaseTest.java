import com.Game.controller.GameController;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;

package com.Game.Phases;



class StartupPhaseTest {

    @Test
    void testStartPhase_AddPlayer() {
        // Mock dependencies
        GameController mockGameController = Mockito.mock(GameController.class);
        CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
        Map mockGameMap = Mockito.mock(Map.class);

        // Command to add a player
        String[] commandParts = {"gameplayer", "-add", "Player1"};

        // Execute phase
        StartupPhase startupPhase = new StartupPhase();
        startupPhase.StartPhase(mockGameController, List.of(), mockCommandPromptView, commandParts, mockGameMap);

        // Verify interactions
        Mockito.verify(mockGameController).handleGamePlayer("-add", "Player1");
        Mockito.verify(mockGameController.getView(), Mockito.never()).displayError(Mockito.anyString());
    }

    @Test
    void testStartPhase_RemovePlayer() {
        // Mock dependencies
        GameController mockGameController = Mockito.mock(GameController.class);
        CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
        Map mockGameMap = Mockito.mock(Map.class);

        // Command to remove a player
        String[] commandParts = {"gameplayer", "-remove", "Player1"};

        // Execute phase
        StartupPhase startupPhase = new StartupPhase();
        startupPhase.StartPhase(mockGameController, List.of(), mockCommandPromptView, commandParts, mockGameMap);

        // Verify interactions
        Mockito.verify(mockGameController).handleGamePlayer("-remove", "Player1");
        Mockito.verify(mockGameController.getView(), Mockito.never()).displayError(Mockito.anyString());
    }

    @Test
    void testStartPhase_InvalidCommand() {
        // Mock dependencies
        GameController mockGameController = Mockito.mock(GameController.class);
        CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
        Map mockGameMap = Mockito.mock(Map.class);

        // Invalid command
        String[] commandParts = {"gameplayer", "-invalid"};

        // Execute phase
        StartupPhase startupPhase = new StartupPhase();
        startupPhase.StartPhase(mockGameController, List.of(), mockCommandPromptView, commandParts, mockGameMap);

        // Verify interactions
        Mockito.verify(mockGameController.getView()).displayError("Usage: gameplayer -add playerName OR gameplayer -remove playerName");
        Mockito.verify(mockGameController, Mockito.never()).handleGamePlayer(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testStartPhase_MissingCommandParts() {
        // Mock dependencies
        GameController mockGameController = Mockito.mock(GameController.class);
        CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
        Map mockGameMap = Mockito.mock(Map.class);

        // Missing command parts
        String[] commandParts = {"gameplayer"};

        // Execute phase
        StartupPhase startupPhase = new StartupPhase();
        startupPhase.StartPhase(mockGameController, List.of(), mockCommandPromptView, commandParts, mockGameMap);

        // Verify interactions
        Mockito.verify(mockGameController.getView()).displayError("Usage: gameplayer -add playerName OR gameplayer -remove playerName");
        Mockito.verify(mockGameController, Mockito.never()).handleGamePlayer(Mockito.anyString(), Mockito.anyString());
    }
}
