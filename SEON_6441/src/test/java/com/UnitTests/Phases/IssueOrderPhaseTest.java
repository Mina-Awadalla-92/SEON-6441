package com.UnitTests.Phases;

import com.Game.controller.GameController;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import com.Game.Phases.IssueOrderPhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.Game.view.GameView;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

public class IssueOrderPhaseTest {

    private GameController gameController;
    private CommandPromptView commandPromptView;
    private Player player1, player2;
    private Map gameMap;
    private IssueOrderPhase issueOrderPhase;
    private GameView gameView;

    @BeforeEach
    void setUp() {
        gameController = mock(GameController.class);
        commandPromptView = mock(CommandPromptView.class);
        gameMap = mock(Map.class);
        gameView = mock(GameView.class);

        player1 = mock(Player.class);
        player2 = mock(Player.class);

        issueOrderPhase = new IssueOrderPhase();

        // Mock the return value of getView() to return the GameView mock
        when(gameController.getView()).thenReturn(gameView);
    }

    @Test
    void testStartPhase_PlayerHasReinforcements_IssuesOrders() {
        // Arrange
        when(player1.getNbrOfReinforcementArmies()).thenReturn(3);
        when(player2.getNbrOfReinforcementArmies()).thenReturn(0);
        when(player1.getName()).thenReturn("Player 1");

        List<Player> players = Arrays.asList(player1, player2);

        // Simulate user input for issuing an order and finishing after one order
        when(commandPromptView.getPlayerOrder(anyString(), anyInt()))
                .thenReturn("order") // First call returns "order"
                .thenReturn("FINISH"); // Second call returns "FINISH"

        // Act
        issueOrderPhase.StartPhase(gameController, players, commandPromptView, new String[]{}, gameMap);

        // Assert
        // Verify interaction with gameController to display player turn
        verify(gameView).displayPlayerTurn("Player 1", 3);

        // Verify that the player is prompted for orders
        verify(commandPromptView, times(2)).getPlayerOrder("Player 1", 3);

        // Verify that player1 issued an order (issueOrder should return true)
        verify(player1).issueOrder("order", gameMap, players);
    }

    @Test
    void testStartPhase_PlayerFailsToIssueOrder() {
        // Arrange
        when(player1.getNbrOfReinforcementArmies()).thenReturn(2);
        when(player2.getNbrOfReinforcementArmies()).thenReturn(0);
        when(player1.getName()).thenReturn("Player 1");

        List<Player> players = Arrays.asList(player1, player2);

        // Simulate failure of issuing an order
        when(commandPromptView.getPlayerOrder(anyString(), anyInt()))
                .thenReturn("order")  // First call: return a failed order
                .thenReturn("FINISH"); // Second call: finish the phase

        // Simulate the failure of issuing the order
        when(player1.issueOrder(anyString(), any(), any())).thenReturn(false);

        // Act
        issueOrderPhase.StartPhase(gameController, players, commandPromptView, new String[]{}, gameMap);

        // Assert
        // Verify the error message is displayed when the order fails
        verify(gameView).displayError("Failed to create order. Please check your command format.");
        // Verify the player is prompted for an order
        verify(commandPromptView, times(2)).getPlayerOrder("Player 1", 2);
        // Verify that player1 attempted to issue the order
        verify(player1).issueOrder("order", gameMap, players);
    }

    @Test
    void testStartPhase_PlayerHasNoReinforcements() {
        // Arrange
        when(player1.getNbrOfReinforcementArmies()).thenReturn(0);
        when(player2.getNbrOfReinforcementArmies()).thenReturn(0);

        List<Player> players = Arrays.asList(player1, player2);

        // Act
        issueOrderPhase.StartPhase(gameController, players, commandPromptView, new String[]{}, gameMap);

        // Assert
        // Verify that no player with 0 reinforcements is prompted to issue orders
        verify(commandPromptView, never()).getPlayerOrder(anyString(), anyInt());
    }

}

