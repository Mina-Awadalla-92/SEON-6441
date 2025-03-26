package com.Game.Phases;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.Game.controller.GameController;
import com.Game.model.Map;
import com.Game.view.CommandPromptView;

class StartupPhaseTest {

    @Test
    void testStartPhase_WhenAddingPlayer() {
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
    void testStartPhase_WhenRemovingPlayer() {
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
    void testStartPhase_WhenCommandIsInvalid() {
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
    void testStartPhase_WhenCommandPartsAreMissing() {
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

    @Test
    void testStartPhase_AddPlayerWithEmptyName() {
        // Mock dependencies
        GameController mockGameController = Mockito.mock(GameController.class);
        CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
        Map mockGameMap = Mockito.mock(Map.class);

        // Command to add a player with an empty name
        String[] commandParts = {"gameplayer", "-add", ""};

        // Execute phase
        StartupPhase startupPhase = new StartupPhase();
        startupPhase.StartPhase(mockGameController, List.of(), mockCommandPromptView, commandParts, mockGameMap);

        // Verify interactions
        Mockito.verify(mockGameController.getView()).displayError("Player name cannot be empty.");
        Mockito.verify(mockGameController, Mockito.never()).handleGamePlayer(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testStartPhase_RemoveNonExistentPlayer() {
        // Mock dependencies
        GameController mockGameController = Mockito.mock(GameController.class);
        CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
        Map mockGameMap = Mockito.mock(Map.class);

        // Command to remove a non-existent player
        String[] commandParts = {"gameplayer", "-remove", "NonExistentPlayer"};

        // Execute phase
        StartupPhase startupPhase = new StartupPhase();
        startupPhase.StartPhase(mockGameController, List.of(), mockCommandPromptView, commandParts, mockGameMap);

        // Verify interactions
        Mockito.verify(mockGameController.getView()).displayError("Player 'NonExistentPlayer' does not exist.");
        Mockito.verify(mockGameController, Mockito.never()).handleGamePlayer(Mockito.anyString(), Mockito.anyString());
    }
}
