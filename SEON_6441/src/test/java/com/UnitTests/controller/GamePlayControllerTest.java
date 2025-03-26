package com.UnitTests.controller;

import com.Game.controller.GameController;
import com.Game.controller.GamePlayController;
import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class GamePlayControllerTest {

    private GameController d_gameControllerMock;
    private Map d_gameMapMock;
    private List<Player> d_playersMock;
    private GamePlayController d_gamePlayController;
    private List<Territory> d_territories;

    @BeforeEach
    void setUp() {
        
        d_gameControllerMock = mock(GameController.class);
        d_gameMapMock = mock(Map.class);
        d_playersMock = new ArrayList<>();
        d_territories = new ArrayList<>();

        // Mock players
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        d_playersMock.add(player1);
        d_playersMock.add(player2);

        // Mock territories (create two sample territories for testing)
        Territory territory1 = mock(Territory.class);
        Territory territory2 = mock(Territory.class);
        List<Territory> territories = new ArrayList<>();
        territories.add(territory1);
        territories.add(territory2);

        // Mock the map's behavior to return territories
        when(d_gameMapMock.getTerritoryList()).thenReturn(territories);

        // Mock GameController's view to display messages
        GameView gameViewMock = mock(GameView.class);
        when(d_gameControllerMock.getView()).thenReturn(gameViewMock);

        // Instantiate the controller with mocked dependencies
        d_gamePlayController = new GamePlayController(d_gameControllerMock, d_gameMapMock, d_playersMock);
    }

    @Test
    void testHandleCommand_showmap() {
        // Mocking necessary behavior
        GameView gameViewMock = mock(GameView.class);
        when(d_gameControllerMock.getView()).thenReturn(gameViewMock);

        // Test the showmap command
        d_gamePlayController.handleCommand(new String[]{"showmap"}, "showmap");

        // Verify the expected interaction
        verify(gameViewMock).displayMap(d_gameMapMock, d_playersMock);
    }

    @Test
    void testHandleCommand_issueorder() {
        // Mocking necessary behavior
        when(d_gameControllerMock.isGameStarted()).thenReturn(true);
        GameView gameViewMock = mock(GameView.class);
        when(d_gameControllerMock.getView()).thenReturn(gameViewMock);

        // Test the issueorder command
        d_gamePlayController.handleCommand(new String[]{"issueorder"}, "issueorder");

        // Verify the expected interactions
        verify(gameViewMock).displayIssueOrdersPhase();
        verify(gameViewMock).displayIssueOrdersComplete();
    }

    @Test
    void testHandleCommand_executeorders() {
        // Mocking necessary behavior
        when(d_gameControllerMock.isGameStarted()).thenReturn(true);
        GameView gameViewMock = mock(GameView.class);
        when(d_gameControllerMock.getView()).thenReturn(gameViewMock);

        // Test the executeorders command
        d_gamePlayController.handleCommand(new String[]{"executeorders"}, "executeorders");

        // Verify the expected interactions
        verify(gameViewMock).displayExecuteOrdersPhase();
        verify(gameViewMock).displayExecuteOrdersComplete();
    }

    @Test
    void testHandleCommand_endturn() {
        // Mocking necessary behavior
        when(d_gameControllerMock.isGameStarted()).thenReturn(true);
        GameView gameViewMock = mock(GameView.class);
        when(d_gameControllerMock.getView()).thenReturn(gameViewMock);

        // Test the endturn command
        d_gamePlayController.handleCommand(new String[]{"endturn"}, "endturn");

        // Verify the expected interactions
        verify(gameViewMock).displayEndTurn();
    }

    @Test
    void testHandleCommand_gameNotStarted() {
        // Mocking necessary behavior to simulate the game not started
        when(d_gameControllerMock.isGameStarted()).thenReturn(false);
        GameView gameViewMock = mock(GameView.class);
        when(d_gameControllerMock.getView()).thenReturn(gameViewMock);

        // Test the issueorder command when the game is not started
        d_gamePlayController.handleCommand(new String[]{"issueorder"}, "issueorder");

        // Verify the error message is displayed
        verify(gameViewMock).displayError("Game has not started yet. Use 'startgame' command.");
    }

    @Test
    void testHandleReinforcement() {
        // Mocking necessary behavior
        when(d_gameControllerMock.isGameStarted()).thenReturn(true);
        GameView gameViewMock = mock(GameView.class);
        when(d_gameControllerMock.getView()).thenReturn(gameViewMock);

        // Test the reinforcement phase
        d_gamePlayController.handleReinforcement();

        // Verify the expected interactions
        verify(gameViewMock).displayReinforcementPhase();
        verify(gameViewMock).displayReinforcementComplete();
    }

    @Test
    void testHandleAssignCountriesWithNotEnoughPlayers() {
        List<Player> d_playersMock = mock(List.class);  // Assuming d_playersMock is a List
        when(d_playersMock.size()).thenReturn(1);
        d_gamePlayController = new GamePlayController(d_gameControllerMock, d_gameMapMock, d_playersMock);
        boolean result = d_gamePlayController.handleAssignCountries();
        assertFalse(result, "Game should not start with less than 2 players.");

        // Verify the error message was displayed
        verify(d_gameControllerMock.getView()).displayError("Need at least 2 players to start the game.");
    }

    @Test
    void testHandleAssignCountriesWithNoTerritories() {
        // Test case: no territories in the map
        List<Player> d_playersMock = mock(List.class);  // Assuming d_playersMock is a List
        when(d_playersMock.size()).thenReturn(2); // 2 players
        when(d_gameMapMock.getTerritoryList()).thenReturn(new ArrayList<>()); // No territories

        boolean result = d_gamePlayController.handleAssignCountries();
        assertFalse(result, "Game should not start with no territories.");

        // Verify the error message was displayed
        verify(d_gameControllerMock.getView()).displayError("No territories in the map. Cannot assign countries.");
    }

    @Test
    void testHandleAssignCountries() {
        // Test case: valid setup with enough players and territories
        List<Player> d_playersMock = mock(List.class);  // Assuming d_playersMock is a List
        when(d_playersMock.size()).thenReturn(2); // 2 players
        Territory territory1 = mock(Territory.class);
        Territory territory2 = mock(Territory.class);
        d_territories.add(territory1);
        d_territories.add(territory2);
        when(d_gameMapMock.getTerritoryList()).thenReturn(d_territories);

        // Here, we remove the mocking of Random
        // Simply use the real logic or remove the dependency on Random
        boolean result = d_gamePlayController.handleAssignCountries();

        assertTrue(result, "Game should assign countries when there are enough players and territories.");

        // Verify the territories are assigned
        verify(territory1).setOwner(any(Player.class));  // Territory should be assigned to a player
        verify(territory2).setOwner(any(Player.class));  // Territory should be assigned to a player
        verify(d_gameControllerMock.getView()).displayMessage("Countries assigned to players:");
    }


    @Test
    void testStartMainGame() {
        // Mocking necessary behavior
        when(d_gameControllerMock.isGameStarted()).thenReturn(true);
        when(d_gameControllerMock.getView()).thenReturn(mock(GameView.class));

        // Test starting the main game
        boolean result = d_gamePlayController.startMainGame();

        // Verify the result and the expected interactions
        assertTrue(result);
    }
}
